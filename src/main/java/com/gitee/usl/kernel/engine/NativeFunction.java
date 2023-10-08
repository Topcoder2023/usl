package com.gitee.usl.kernel.engine;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.infra.proxy.MethodInterceptor;
import com.gitee.usl.infra.structure.Plugins;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 针对 AviatorFunction 原生接口实现类进行代理的函数
 *
 * @author hongda.li
 */
public class NativeFunction extends MethodInterceptor<AviatorFunction> implements FunctionPluggable {
    private final Plugins plugins;
    private final FunctionDefinition definition;

    public NativeFunction(FunctionDefinition definition, Object target) {
        super(target, AviatorFunction.class);
        this.definition = definition;
        this.plugins = new Plugins();
    }

    @Override
    protected boolean filter(Method method) {
        return METHOD_NAME.equals(method.getName());
    }

    @Override
    protected Object intercept(Invocation<AviatorFunction> invocation, Object proxy) {
        Object[] parameters = invocation.args();
        Assert.isTrue(parameters.length >= NumberConstant.ONE);

        // 理论上，Env一定存在
        Env env = (Env) parameters[0];

        // 根据AviatorFunction的实现逻辑不同，这里 objects 参数个数不固定
        AviatorObject[] args = Arrays.stream(ArrayUtil.sub(parameters, NumberConstant.ONE, parameters.length))
                .map(AviatorObject.class::cast)
                .toArray(AviatorObject[]::new);

        // 交由插件增强部分功能
        FunctionSession session = FunctionPluggable.super.buildSession(env, args, this.definition);
        session.setInvocation(invocation);

        return this.withPlugin(session);
    }

    @Override
    public Object handle(FunctionSession session) {
        return session.invocation().invoke();
    }

    @Override
    public Plugins plugins() {
        return this.plugins;
    }

    public FunctionDefinition definition() {
        return this.definition;
    }
}
