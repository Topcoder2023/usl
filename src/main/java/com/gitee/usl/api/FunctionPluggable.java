package com.gitee.usl.api;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.FailurePlugin;
import com.gitee.usl.api.plugin.FinallyPlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.infra.exception.UslExecuteException;
import com.gitee.usl.infra.structure.Plugins;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Optional;

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
     * 获取插件集合
     *
     * @return 插件集合
     */
    Plugins plugins();

    /**
     * 将插件逻辑编织到实际处理逻辑前、成功、失败、后
     *
     * @param session 本次函数调用会话
     * @return 最终返回值
     */
    default AviatorObject withPlugin(final FunctionSession session) {
        try {
            // 执行前置回调插件
            this.plugins().execute(BeginPlugin.class, plugin -> plugin.onBegin(session));

            // 正常来说执行结果还没有被初始化，这里应该为空
            // 但如果不为空，说明前置插件已经设置了本次调用返回值
            // 那么就直接将前置插件的返回值作为最终结果
            if (session.result() != null) {
                return FunctionUtils.wrapReturn(session.result());
            }

            // 调用实际处理逻辑
            Object result = this.handle(session);

            // 设置当前调用的返回值
            session.setResult(result);

            // 执行成功回调插件
            this.plugins().execute(SuccessPlugin.class, plugin -> plugin.onSuccess(session));

            // 统一包装返回值
            // 这里的返回值取的是调用会话中的返回值
            // 也就意味着执行成功回调插件可以改变返回值
            return FunctionUtils.wrapReturn(session.result());
        } catch (Exception e) {

            // 设置当前调用的异常
            session.setException(e);

            // 设置失败回调插件
            this.plugins().execute(FailurePlugin.class, plugin -> plugin.onFailure(session));

            // 正常来说当前调用异常一定不为空
            // 但是如果为空说明失败回调插件清空了当前调用异常
            // 那么就直接返回调用会话中的返回值
            Optional.ofNullable(session.exception()).ifPresent(error -> {
                // 如果调用异常不为空，则将调用异常统一包装为 USL-Execute 异常
                // 这样做是为了更好的区分整个脚本执行周期中的异常来源
                if (error instanceof UslExecuteException ue) {
                    throw ue;
                } else {
                    throw new UslExecuteException(error);
                }
            });

            // 返回并包装调用会话中的返回值
            return FunctionUtils.wrapReturn(session.result());
        } finally {

            // 执行最终回调插件
            this.plugins().execute(FinallyPlugin.class, plugin -> plugin.onFinally(session));
        }
    }
}
