package com.gitee.usl.infra.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.FunctionMissing;
import com.gitee.usl.grammar.runtime.type._Object;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 此类作为 FunctionMissing 接口的默认实现,
 * 在启用了方法调用配置的前提上,会解析 <变量名.方法名()>，并通过反射传入参数并获取其返回值
 *
 * @author hongda.li
 */
@Getter
public class MethodInvokerOnMissing implements FunctionMissing {

    @Description("是否启用")
    private final boolean enabled;

    @Description("自定义机制")
    private final FunctionMissing functionMissing;

    @Description("方法调用标识")
    private static final String REGEX = "\\.";

    public MethodInvokerOnMissing(boolean enabled) {
        this(enabled, DEFAULT);
    }

    public MethodInvokerOnMissing(boolean enabled, FunctionMissing functionMissing) {
        this.enabled = enabled;
        this.functionMissing = functionMissing;
    }

    @Override
    public _Object onFunctionMissing(String name, Map<String, Object> env, _Object... arguments) {

        if (!enabled) {
            return functionMissing.onFunctionMissing(name, env, arguments);
        }

        @Description("<变量名.方法名()>")
        String[] metaInfo = name.split(REGEX);

        if (metaInfo.length < 2) {
            return functionMissing.onFunctionMissing(name, env, arguments);
        }

        @Description("变量名")
        String targetName = metaInfo[0];

        @Description("方法名")
        String methodName = metaInfo[1];

        @Description("变量实例")
        Object target = env.get(targetName);

        if (target == null) {
            return functionMissing.onFunctionMissing(name, env, arguments);
        }

        @Description("方法实例")
        Method method = ReflectUtil.getMethodByName(target.getClass(), methodName);

        if (method == null) {
            return functionMissing.onFunctionMissing(name, env, arguments);
        }

        Assert.isTrue(metaInfo.length == 2, "不支持链式调用对象方法");

        if (ArrayUtil.isEmpty(arguments)) {
            return ReflectUtil.invoke(target, method);
        } else {
            return ReflectUtil.invoke(target, method, Arrays.stream(arguments).map(arg -> arg.getValue(env)).toArray());
        }
    }

}
