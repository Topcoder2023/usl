package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.api.Definable;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.api.annotation.Description;
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
 * @author hongda.li
 */
@Description("原生接口实现类进行代理的函数")
public class NativeFunction extends MethodInterceptor<AviatorFunction> implements FunctionPluggable, Definable {

    @Description("插件链")
    private final Plugins plugins = new Plugins();

    @Description("函数定义")
    private final FunctionDefinition definition;

    public NativeFunction(FunctionDefinition definition, Object target) {
        super(target, AviatorFunction.class);
        this.definition = definition;
    }

    @Override
    protected boolean filter(Method method) {
        return METHOD_NAME.equals(method.getName());
    }

    @Override
    protected Object intercept(Invocation<AviatorFunction> invocation, Object proxy) {

        @Description("原始参数列表")
        Object[] parameters = invocation.args();

        @Description("上下文参数")
        Env env = (Env) parameters[0];

        @Description("函数实参")
        AviatorObject[] args = Arrays.stream(ArrayUtil.sub(parameters, NumberConstant.ONE, parameters.length))
                .map(AviatorObject.class::cast)
                .toArray(AviatorObject[]::new);

        @Description("函数调用会话")
        FunctionSession session = new FunctionSession(env, args, this.definition);
        session.setInvocation(invocation);
        session.setHandler(this::handle);

        return this.withPlugin(session);
    }

    @Override
    public Object handle(FunctionSession session) {
        return session.getInvocation().invoke();
    }

    @Override
    public Plugins plugins() {
        return this.plugins;
    }

    @Override
    public FunctionDefinition definition() {
        return this.definition;
    }

}
