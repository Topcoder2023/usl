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
import lombok.Getter;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
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

        this.bindSession(session);

        try {
            SharedSession.setSession(session);
            return FunctionUtils.wrapReturn(session.getInvocation().invoke());
        } finally {
            SharedSession.clear();
        }
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    private void bindSession(FunctionSession session) {
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
            args.add(this.bindSession(i, env, expectArgs.get(i), actualArgs));
        }
        session.setInvocation(methodMeta.toInvocation(args));
    }

    private Object bindSession(int index, Env env, Parameter parameter, AviatorObject[] actualArgs) {
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
