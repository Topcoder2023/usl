package com.gitee.usl.infra.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.UslNotFoundException;
import com.googlecode.aviator.FunctionMissing;
import com.googlecode.aviator.runtime.type.AviatorObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class MethodInvokerOnMissing implements FunctionMissing {

    @Description("是否启用")
    private boolean enabled;

    @Description("自定义机制")
    private FunctionMissing functionMissing;

    @Description("方法调用标识")
    private static final String REGEX = "\\.";

    @Description("异常消费者")
    private static final Function<String, AviatorObject> THROW_ERROR = name -> {
        throw new UslNotFoundException("无法加载此函数 - {}", name);
    };

    @Override
    public AviatorObject onFunctionMissing(String name, Map<String, Object> env, AviatorObject... args) {

        if (functionMissing == null) {
            functionMissing = (arg1, arg2, arg3) -> THROW_ERROR.apply(arg1);
        }

        if (!enabled) {
            return functionMissing.onFunctionMissing(name, env, args);
        }

        String[] metaInfo = name.split(REGEX);

        if (metaInfo.length < 2) {
            return THROW_ERROR.apply(name);
        }

        String targetName = metaInfo[0];
        String methodName = metaInfo[1];

        Object target = env.get(targetName);

        if (target == null) {
            return THROW_ERROR.apply(name);
        }

        Method method = ReflectUtil.getMethodByName(target.getClass(), methodName);

        if (method == null) {
            return THROW_ERROR.apply(name);
        }

        Assert.isTrue(metaInfo.length == 2, "不支持链式调用对象方法");

        if (ArrayUtil.isEmpty(args)) {
            return ReflectUtil.invoke(target, method);
        } else {
            return ReflectUtil.invoke(target, method, Arrays.stream(args).map(arg -> arg.getValue(env)).toArray());
        }
    }
}
