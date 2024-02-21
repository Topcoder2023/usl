package com.gitee.usl.kernel.plugin;

import cn.hutool.core.convert.Convert;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.infra.structure.wrapper.ParameterWrapper;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * 参数绑定插件
 *
 * @author hongda.li
 */
@Order(ParameterBinderPlugin.PARAM_BINDER_ORDER)
public class ParameterBinderPlugin implements BeginPlugin {

    /**
     * 参数绑定插件生效的优先级
     */
    public static final int PARAM_BINDER_ORDER = Integer.MIN_VALUE + 10;

    @Override
    public void onBegin(FunctionSession session) {

        // 方法元数据
        MethodMeta<?> methodMeta = session.getDefinition().getMethodMeta();

        // 无参函数，无需绑定
        if (methodMeta.isNoArgs()) {
            session.setInvocation(methodMeta.toInvocation());
            return;
        }

        // 上下文环境
        Env env = session.getEnv();

        // 实参列表
        _Object[] actualArgs = session.getObjects();

        // 形参列表
        List<ParameterWrapper> expectArgs = methodMeta.getParameterWrapperList();

        // 参数绑定
        Object[] args = expectArgs.stream()
                .map(expectArg -> this.bind(env, expectArg, actualArgs))
                .toArray(Object[]::new);

        // 将方法元属性转为方法调用器
        session.setInvocation(methodMeta.toInvocation(args));
    }

    /**
     * 参数绑定，将 _Object 参数转换为具体类型的参数
     *
     * @param expectArg  函数形参
     * @param env        上下文环境
     * @param actualArgs 实参列表
     * @return 转换后的参数
     */
    protected Object bind(Env env, ParameterWrapper expectArg, _Object[] actualArgs) {
        // 形参索引
        int index = expectArg.getIndex();

        // 形参实例
        Parameter parameter = expectArg.get();

        // 形参类型
        Class<?> type = parameter.getType();

        // 实参长度
        int actualLength = actualArgs.length;

        // 避免数组越界，直接用空值填充，此处可能产生的原因是方法定义了N个参数，但实际传入的参数小于N
        if (index > actualLength - 1) {
            return null;
        }

        // 原始类型，无需转换
        if (_Object.class.isAssignableFrom(type)) {
            return actualArgs[index];
        }

        if (type.isArray()) {
            // 数组元素的实际类型
            Class<?> elementType = type.getComponentType();

            // 构造数组长度为：实际传入参数个数 - 已绑定的参数个数
            Object array = Array.newInstance(elementType, actualArgs.length - index);

            // 内循环次数标识
            IntWrapper loop = new IntWrapper(index);

            while (loop.get() < actualArgs.length) {
                Array.set(array, loop.get() - index, _Object.class.isAssignableFrom(elementType)
                        ? actualArgs[loop.get()]
                        : Convert.convert(elementType, actualArgs[loop.get()].getValue(env)));
                loop.increment();
            }

            return array;
        }

        return Convert.convert(type, actualArgs[index].getValue(env));
    }

}
