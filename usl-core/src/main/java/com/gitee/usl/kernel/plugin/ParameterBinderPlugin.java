package com.gitee.usl.kernel.plugin;

import cn.hutool.core.convert.Convert;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.IntStream;

/**
 * @author hongda.li
 */
@Description("参数绑定插件")
public class ParameterBinderPlugin implements BeginPlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("ReassignedVariable")
    @Override
    public void onBegin(FunctionSession session) {
        MethodMeta<?> methodMeta = session.getDefinition().getMethodMeta();

        // 跳过无参函数
        Method method = methodMeta.getMethod();
        if (method.getParameterCount() == NumberConstant.ZERO) {
            session.setInvocation(methodMeta.toInvocation(null));
            return;
        }

        // 当前方法的参数列表及预期的参数长度
        Parameter[] parameters = method.getParameters();
        int length = parameters.length;

        // 当前函数的实际参数及实际的参数长度
        AviatorObject[] objects = session.getObjects();
        int maxLength = objects.length;

        // 当前的上下文环境
        final Env env = session.getEnv();

        IntWrapper wrapper = new IntWrapper();

        Object[] args = IntStream.range(NumberConstant.ZERO, length)
                .filter(index -> index < length)
                .mapToObj(index -> {
                    // 当前参数的类型
                    Parameter parameter = parameters[index];
                    Class<?> type = parameter.getType();

                    // 如果是 Env 类型的参数，则返回上下文环境
                    if (Env.class.equals(type)) {
                        logger.debug("[参数绑定] - 自动绑定上下文环境");
                        wrapper.increment();
                        return env;
                    }

                    // 如果是 USLRunner 类型的参数，则返回当前使用的USL实例
                    if (USLRunner.class.equals(type)) {
                        logger.debug("[参数绑定] - 自动绑定USL实例");
                        wrapper.increment();
                        return session.getDefinition().getRunner();
                    }

                    // 如果是 FunctionSession 类型的参数，则返回会话信息
                    if (FunctionSession.class.equals(type)) {
                        logger.debug("[参数绑定] - 自动绑定会话信息");
                        wrapper.increment();
                        return session;
                    }

                    // 避免数组越界，直接用 null 填充
                    // 此处可能产生的原因是方法定义了 N 个参数，但实际传入的参数小于 N
                    if (index - wrapper.get() > maxLength - 1) {
                        return null;
                    }

                    // 如果是 AviatorObject 类型的参数，则直接返回
                    if (AviatorObject.class.isAssignableFrom(type)) {
                        return objects[index - wrapper.get()];
                    }

                    // 如果是数组或可变长度的参数
                    if (type.isArray()) {
                        Object array;
                        Class<?> elementType = type.getComponentType();
                        // 构造一个长度为 实际传入参数个数 - 已绑定的参数个数(除来自于环境变量以外的参数) 的数组
                        array = Array.newInstance(elementType, objects.length - index + wrapper.get());
                        boolean isRaw = AviatorObject.class.isAssignableFrom(elementType);

                        // 将剩余所有参数都填充到这个数组中
                        for (int j = index - wrapper.get(); j < objects.length; j++) {
                            Object converted = Convert.convert(elementType, objects[j].getValue(env));
                            Array.set(array, j - index + wrapper.get(), isRaw ? objects[j] : converted);
                        }
                        return array;
                    }

                    Object unconverted = objects[index - wrapper.get()].getValue(env);

                    // 如无需转换则不进行转换
                    if (unconverted != null && type.isAssignableFrom(unconverted.getClass())) {
                        return type.cast(unconverted);
                    }

                    return Convert.convert(type, unconverted);
                })
                .toArray();

        // 构建参数绑定逻辑完成后调用信息
        session.setInvocation(methodMeta.toInvocation(args));
    }
}
