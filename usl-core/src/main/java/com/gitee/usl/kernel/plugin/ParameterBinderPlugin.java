package com.gitee.usl.kernel.plugin;

import cn.hutool.core.convert.Convert;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.infra.structure.wrapper.ParameterWrapper;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @author hongda.li
 */
@Description("参数绑定插件")
public class ParameterBinderPlugin implements BeginPlugin {

    @Override
    public void onBegin(FunctionSession session) {

        @Description("方法元数据")
        MethodMeta<?> methodMeta = session.getDefinition().getMethodMeta();

        if (methodMeta.isNoArgs()) {
            session.setInvocation(methodMeta.toInvocation());
            return;
        }

        @Description("上下文环境")
        Env env = session.getEnv();

        @Description("额外参数数量")
        IntWrapper additionalCount = new IntWrapper();

        @Description("实参列表")
        AviatorObject[] actualArgs = session.getObjects();

        @Description("形参列表")
        List<ParameterWrapper> expectArgs = methodMeta.getParameterWrapperList();

        @Description("参数绑定")
        Object[] args = expectArgs.stream()
                .map(expectArg -> this.bind(expectArg, additionalCount, env, session, actualArgs))
                .toArray(Object[]::new);

        session.setInvocation(methodMeta.toInvocation(args));
    }

    @Description("参数绑定，将AviatorObject参数转换为具体类型的参数")
    protected Object bind(@Description("函数形参") ParameterWrapper expectArg,
                          @Description("额外参数数量") IntWrapper additionalCount,
                          @Description("上下文环境") Env env,
                          @Description("函数调用会话") FunctionSession session,
                          @Description("实参列表") AviatorObject[] actualArgs) {
        @Description("形参索引")
        int index = expectArg.getIndex();

        @Description("形参实例")
        Parameter parameter = expectArg.get();

        @Description("形参类型")
        Class<?> type = parameter.getType();

        @Description("实参长度")
        int actualLength = actualArgs.length;

        if (Env.class.equals(type)) {
            additionalCount.increment();
            return env;
        }

        if (USLRunner.class.equals(type)) {
            additionalCount.increment();
            return session.getDefinition().getRunner();
        }

        if (FunctionSession.class.equals(type)) {
            additionalCount.increment();
            return session;
        }

        @Description("避免数组越界，直接用空值填充，此处可能产生的原因是方法定义了N个参数，但实际传入的参数小于N")
        boolean outOfLength = index - additionalCount.get() > actualLength - 1;
        if (outOfLength) {
            return null;
        }

        if (AviatorObject.class.isAssignableFrom(type)) {
            return actualArgs[index - additionalCount.get()];
        }

        @Description("数组参数或可变长度的参数")
        boolean isArray = type.isArray();

        if (isArray) {
            @Description("数组元素的实际类型")
            Class<?> elementType = type.getComponentType();

            @Description("构造数组长度为：实际传入参数个数 - 已绑定的参数个数(除来自于环境变量以外的参数)")
            Object array = Array.newInstance(elementType, actualArgs.length - index + additionalCount.get());

            @Description("内循环次数标识")
            IntWrapper loop = new IntWrapper(index - additionalCount.get());

            while (loop.get() < actualArgs.length) {
                Array.set(array, loop.get() - index + additionalCount.get(), AviatorObject.class.isAssignableFrom(elementType)
                        ? actualArgs[loop.get()]
                        : Convert.convert(elementType, actualArgs[loop.get()].getValue(env)));
                loop.increment();
            }

            return array;
        }

        @Description("尚未转换的实际值")
        Object unconverted = actualArgs[index - additionalCount.get()].getValue(env);

        @Description("无需转换标识")
        boolean skipConvert = unconverted != null && type.isAssignableFrom(unconverted.getClass());

        if (skipConvert) {
            return type.cast(unconverted);
        }

        return Convert.convert(type, unconverted);
    }

}
