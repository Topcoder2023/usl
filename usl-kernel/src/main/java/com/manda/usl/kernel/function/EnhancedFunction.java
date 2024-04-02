package com.manda.usl.kernel.function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import com.manda.usl.kernel.entity.FunctionMeta;
import com.manda.usl.kernel.entity.FunctionSession;
import com.manda.usl.kernel.entity.MethodMeta;
import com.manda.usl.kernel.entity.SharedSession;
import com.manda.usl.kernel.plugin.*;
import lombok.Getter;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 增强型函数
 * 基于可变参数函数进行增强
 *
 * @author hongda.li
 */
@Getter
@SuppressWarnings("ReassignedVariable")
public class EnhancedFunction extends AbstractVariadicFunction {

    private final FunctionMeta definition;

    public EnhancedFunction(FunctionMeta definition) {
        this.definition = definition;
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        FunctionSession session = new FunctionSession((Env) env, args, this.definition);

        this.bindParams(session);

        try {
            // 绑定会话
            SharedSession.setSession(session);

            // 执行前置回调插件
            this.invokePlugin(BeginPlugin.class, plugin -> plugin.onBegin(session));

            // 正常来说执行结果还没有被初始化，这里应该为空
            // 但如果不为空，说明前置插件已经设置了本次调用返回值
            // 那么就直接将前置插件的返回值作为最终结果
            if (session.getResult() != null) {
                // 如果前置插件设置了返回值，则会被视为执行成功
                // 因此同样会执行成功回调插件
                this.invokePlugin(SuccessPlugin.class, plugin -> plugin.onSuccess(session));

                // 统一包装返回值
                return FunctionUtils.wrapReturn(session.getResult());
            }

            Object result = session.getInvocation().invoke();

            // 设置当前调用的返回值
            session.setResult(result);

            // 执行成功回调插件
            this.invokePlugin(SuccessPlugin.class, plugin -> plugin.onSuccess(session));

            // 统一包装返回值
            // 这里的返回值取的是调用会话中的返回值
            // 也就意味着执行成功回调插件可以改变返回值
            return FunctionUtils.wrapReturn(session.getResult());
        } catch (Exception e) {
            // 设置当前调用的异常
            session.setException(e);

            // 设置失败回调插件
            this.invokePlugin(FailurePlugin.class, plugin -> plugin.onFailure(session));

            // 正常来说当前调用异常一定不为空
            // 但是如果为空说明失败回调插件清空了当前调用异常
            // 那么就直接返回调用会话中的返回值
            Optional.ofNullable(session.getException()).ifPresent(error -> {
                // 如果调用异常不为空，则将调用异常统一包装为 USL-Execute 异常
                // 这样做是为了更好的区分整个脚本执行周期中的异常来源
                throw new RuntimeException(error);
            });

            // 返回并包装调用会话中的返回值
            return FunctionUtils.wrapReturn(session.getResult());
        } finally {
            // 清除会话
            SharedSession.clear();

            // 执行最终回调插件
            this.invokePlugin(FinallyPlugin.class, plugin -> plugin.onFinally(session));
        }
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    private <P extends Plugin> void invokePlugin(Class<P> type, Consumer<P> consumer) {
        List<Plugin> pluginList = definition.getPluginList();
        if (CollUtil.isEmpty(pluginList)) {
            return;
        }
        pluginList.stream()
                .filter(plugin -> type.isAssignableFrom(plugin.getClass()))
                .map(type::cast)
                .forEach(consumer);
    }

    private void bindParams(FunctionSession session) {
        MethodMeta methodMeta = session.getDefinition().getMethodMeta();
        if (methodMeta.method().getParameterCount() == 0) {
            session.setInvocation(methodMeta.toInvocation(null));
            return;
        }
        Env env = session.getEnv();
        AviatorObject[] actualArgs = session.getObjects();
        List<Parameter> expectArgs = CollUtil.newArrayList(methodMeta.method().getParameters());
        List<Object> args = new ArrayList<>();
        for (int i = 0; i < expectArgs.size(); i++) {
            args.add(this.bindParams(i, env, expectArgs.get(i), actualArgs));
        }
        session.setInvocation(methodMeta.toInvocation(args));
    }

    private Object bindParams(int index, Env env, Parameter parameter, AviatorObject[] actualArgs) {
        Class<?> type = parameter.getType();
        if (index > actualArgs.length - 1) {
            return null;
        }
        if (AviatorObject.class.isAssignableFrom(type)) {
            return actualArgs[index];
        }
        if (type.isArray()) {
            Class<?> elementType = type.getComponentType();
            Object array = Array.newInstance(elementType, actualArgs.length - index);
            int loop = index;
            while (loop < actualArgs.length) {
                Array.set(array, loop - index, AviatorObject.class.isAssignableFrom(elementType)
                        ? actualArgs[loop]
                        : Convert.convert(elementType, actualArgs[loop].getValue(env)));
                loop++;
            }
            return array;
        }
        return Convert.convert(type, actualArgs[index].getValue(env));
    }
}
