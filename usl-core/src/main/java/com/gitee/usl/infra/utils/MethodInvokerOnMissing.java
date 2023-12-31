package com.gitee.usl.infra.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLNotFoundException;
import com.googlecode.aviator.FunctionMissing;
import com.googlecode.aviator.runtime.type.AviatorObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
@Description({"此类作为FunctionMissing接口的默认实现",
        "在启用了方法调用配置的前提上",
        "会解析<变量名.方法名()>，并通过反射传入参数并获取其返回值"})
public class MethodInvokerOnMissing implements FunctionMissing {

    @Description("是否启用")
    private boolean enabled;

    @Description("自定义机制")
    private FunctionMissing functionMissing;

    @Description("方法调用标识")
    private static final String REGEX = "\\.";

    @Description("异常消费者")
    private static final FunctionMissing DEFAULT_IMPL = (name, env, args) -> {
        throw new USLNotFoundException("无法加载此函数 - {}", name);
    };

    @Override
    public AviatorObject onFunctionMissing(String name, Map<String, Object> env, AviatorObject... args) {

        if (functionMissing == null) {
            functionMissing = DEFAULT_IMPL;
        }

        if (!enabled) {
            return functionMissing.onFunctionMissing(name, env, args);
        }

        @Description("<变量名.方法名()>")
        String[] metaInfo = name.split(REGEX);

        if (metaInfo.length < 2) {
            return functionMissing.onFunctionMissing(name, env, args);
        }

        @Description("变量名")
        String targetName = metaInfo[0];

        @Description("方法名")
        String methodName = metaInfo[1];

        @Description("变量实例")
        Object target = env.get(targetName);

        if (target == null) {
            return functionMissing.onFunctionMissing(name, env, args);
        }

        @Description("方法实例")
        Method method = ReflectUtil.getMethodByName(target.getClass(), methodName);

        if (method == null) {
            return functionMissing.onFunctionMissing(name, env, args);
        }

        Assert.isTrue(metaInfo.length == 2, "不支持链式调用对象方法");

        if (ArrayUtil.isEmpty(args)) {
            return ReflectUtil.invoke(target, method);
        } else {
            return ReflectUtil.invoke(target, method, Arrays.stream(args).map(arg -> arg.getValue(env)).toArray());
        }
    }
}
