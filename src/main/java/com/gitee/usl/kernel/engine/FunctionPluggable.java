package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.exception.UslExecuteException;
import com.gitee.usl.kernel.plugin.*;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 可插件化的接口
 *
 * @author hongda.li
 */
public interface FunctionPluggable {
    /**
     * 实际的处理逻辑
     *
     * @param session 函数调用会话
     * @return 执行结果
     */
    Object handle(final FunctionSession session);

    /**
     * 将插件逻辑编织到实际处理逻辑前、成功、失败、后
     *
     * @param env  执行上下文
     * @param args 调用的原始参数
     * @return 最终返回值
     */
    default AviatorObject withPlugin(FunctionDefinition definition, Map<String, Object> env, AviatorObject... args) {
        // 封装本次调用会话
        final FunctionSession session = new FunctionSession((Env) env, args, definition);

        // 执行前置回调插件
        // 注意，如果前置回调插件出现异常是不会被失败回调插件捕获
        // 这样设计的理由是，前置回调插件一定是在实际执行逻辑前调用
        // 前置回调插件有责任负责处理插件逻辑中可能出现的异常
        this.makePlugin(BeginPlugin.class, plugin -> plugin.onBegin(session));

        // 正常来说执行结果还没有被初始化，这里应该为空
        // 但如果不为空，说明前置插件已经设置了本次调用返回值
        // 那么就直接将前置插件的返回值作为最终结果
        if (session.getResult() != null) {
            return FunctionUtils.wrapReturn(session.getResult());
        }

        try {
            // 调用实际处理逻辑
            Object result = this.handle(session);

            // 设置当前调用的返回值
            session.setResult(result);

            // 执行成功回调插件
            this.makePlugin(SuccessPlugin.class, plugin -> plugin.onSuccess(session));

            // 统一包装返回值
            // 这里的返回值取的是调用会话中的返回值
            // 也就意味着执行成功回调插件可以改变返回值
            return FunctionUtils.wrapReturn(session.getResult());
        } catch (Exception e) {

            // 设置当前调用的异常
            session.setException(e);

            // 设置失败回调插件
            this.makePlugin(FailurePlugin.class, plugin -> plugin.onFailure(session));

            // 正常来说当前调用异常一定不为空
            // 但是如果为空说明失败回调插件清空了当前调用异常
            // 那么就直接返回调用会话中的返回值
            Optional.ofNullable(session.getException()).ifPresent(error -> {
                // 如果调用异常不为空，则将调用异常统一包装为 USL-Execute 异常
                // 这样做是为了更好的区分整个脚本执行周期中的异常来源
                if (error instanceof UslExecuteException ue) {
                    throw ue;
                } else {
                    throw new UslExecuteException(error);
                }
            });

            // 返回并包装调用会话中的返回值
            return FunctionUtils.wrapReturn(session.getResult());
        } finally {

            // 执行最终回调插件
            this.makePlugin(FinallyPlugin.class, plugin -> plugin.onFinally(session));
        }
    }

    /**
     * 获取插件集合
     *
     * @return 插件集合
     */
    List<Plugin> plugins();

    /**
     * 执行插件
     *
     * @param pluginType 插件类型
     * @param consumer   插件消费者
     * @param <T>        插件泛型
     */
    private <T> void makePlugin(Class<T> pluginType, Consumer<T> consumer) {
        plugins().stream()
                .filter(plugin -> pluginType.isAssignableFrom(plugin.getClass()))
                .map(pluginType::cast)
                .forEach(consumer);
    }
}
