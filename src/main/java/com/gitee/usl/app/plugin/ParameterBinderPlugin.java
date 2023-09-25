package com.gitee.usl.app.plugin;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import com.gitee.usl.api.annotation.Env;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.infra.utils.NumberUtil;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.binder.UslConverter;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.kernel.plugin.UslBeginPlugin;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author hongda.li
 */
public class ParameterBinderPlugin implements UslBeginPlugin {
    private final List<UslConverter> converterList;

    public ParameterBinderPlugin() {
        converterList = SpiServiceUtil.services(UslConverter.class);
    }

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

        com.googlecode.aviator.utils.Env env = session.getEnv();

        Object[] args = IntStream.range(NumberConstant.ZERO, length)
                .filter(index -> index < length)
                .mapToObj(index -> {
                    Parameter parameter = parameters[index];
                    Class<?> type = parameter.getType();

                    String envName = AnnotationUtil.getAnnotationValue(parameter, Env.class);

                    if (envName != null) {
                        wrapper.increment();
                        return env.get(envName);
                    }

                    if (com.googlecode.aviator.utils.Env.class.equals(type)) {
                        return env;
                    }

                    Object unconverted = Optional.ofNullable(session.getObjects()[index - wrapper.get()])
                            .map(obj -> obj.getValue(env))
                            .orElse(null);

                    return Convert.convert(type, unconverted);
                })
                .toArray();

        // 构建参数绑定逻辑完成后调用信息
        Invocation<?> bind = new Invocation<>(invocation.target(), invocation.targetType(), method, args);
        definition.setInvocation(bind);
    }
}
