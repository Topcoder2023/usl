package com.manda.usl.kernel.function;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.googlecode.aviator.FunctionMissing;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.manda.usl.kernel.common.FuncException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author hongda.li
 */
public class FunctionFallback implements FunctionMissing {

    private static final String REGEX = "\\.";

    @Override
    public AviatorObject onFunctionMissing(String name,
                                           Map<String, Object> env,
                                           AviatorObject... arguments) {
        String[] metaInfo = name.split(REGEX);

        if (metaInfo.length < 2) {
            throw new FuncException("No such function : {}", name);
        }

        // 变量名
        String targetName = metaInfo[0];

        // 方法名
        String methodName = metaInfo[1];

        // 变量实例
        Object target = env.get(targetName);

        if (target == null) {
            throw new FuncException("No such variable : {}", name);
        }

        // 方法实例
        Method method = ReflectUtil.getMethodByName(target.getClass(), methodName);

        if (method == null) {
            throw new FuncException("No such method : {}", name);
        }

        Assert.isTrue(metaInfo.length == 2, "Chained calling object methods are not supported");

        if (ArrayUtil.isEmpty(arguments)) {
            return FunctionUtils.wrapReturn(ReflectUtil.invoke(target, method));
        } else {
            return FunctionUtils.wrapReturn(ReflectUtil.invoke(target, method, Arrays.stream(arguments).map(arg -> arg.getValue(env)).toArray()));
        }
    }
}
