package com.gitee.usl.app.plugin;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.api.annotation.Var;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.infra.utils.NumberUtil;
import com.gitee.usl.kernel.binder.ConverterFactory;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.kernel.plugin.BeginPlugin;
import com.googlecode.aviator.utils.Env;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author hongda.li
 */
public class ParameterBinderPlugin implements BeginPlugin {

    @Override
    public void onBegin(FunctionSession session) {
        FunctionDefinition definition = session.getDefinition();

        Invocation<?> invocation = definition.getInvocation();

        // 跳过无参函数
        Method method = invocation.method();
        if (method.getParameterCount() == NumberConstant.ZERO) {
            return;
        }

        Parameter[] parameters = method.getParameters();
        int length = parameters.length;

        NumberUtil.IntWrapper wrapper = NumberUtil.ofIntWrapper();

        Env env = session.getEnv();

        Object[] args = IntStream.range(NumberConstant.ZERO, length)
                .filter(index -> index < length)
                .mapToObj(index -> {
                    Parameter parameter = parameters[index];
                    Class<?> type = parameter.getType();

                    if (Env.class.equals(type)) {
                        return env;
                    }

                    Object unconverted = Optional.ofNullable(AnnotationUtil.getAnnotationValue(parameter, Var.class))
                            .map(name -> {
                                wrapper.increment();
                                return this.load(String.valueOf(name), env);
                            })
                            .orElseGet(() -> Optional.ofNullable(session.getObjects()[index - wrapper.get()])
                                    .map(obj -> obj.getValue(env))
                                    .orElse(null));

                    // 如无需转换则不进行转换
                    if (unconverted != null && type.isAssignableFrom(unconverted.getClass())) {
                        return type.cast(unconverted);
                    }

                    return ConverterFactory.getInstance().getConverter(type).convert(unconverted);
                })
                .toArray();

        // 构建参数绑定逻辑完成后调用信息
        Invocation<?> bind = new Invocation<>(invocation.target(), invocation.targetType(), method, args);
        definition.setInvocation(bind);
    }

    /**
     * 加载环境变量的值
     *
     * @param name 环境变量的名称
     * @param env  当前上下文环境
     * @return 环境变量的值
     */
    private Object load(String name, Env env) {
        Object fromEnv;
        if ((fromEnv = env.get(name)) != null) {
            return fromEnv;
        }

        Object fromSystem;
        if ((fromSystem = System.getenv(name)) != null) {
            return fromSystem;
        }

        Object fromProperties;
        if ((fromProperties = System.getProperty(name)) != null) {
            return fromProperties;
        }

        return null;
    }
}
