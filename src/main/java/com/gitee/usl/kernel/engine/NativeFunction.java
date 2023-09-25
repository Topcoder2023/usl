package com.gitee.usl.kernel.engine;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.infra.proxy.MethodInterceptor;
import com.gitee.usl.kernel.plugin.UslPlugin;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 针对 AviatorFunction 原生接口实现类进行代理的函数
 *
 * @author hongda.li
 */
public class NativeFunction extends MethodInterceptor<AviatorFunction> implements UslFunctionPluggable {
    private static final String METHOD_NAME = "call";
    private final List<UslPlugin> pluginList;
    private final FunctionDefinition definition;

    public NativeFunction(FunctionDefinition definition, Object target) {
        super(target, AviatorFunction.class);
        this.definition = definition;
        this.pluginList = new ArrayList<>(NumberConstant.COMMON_SIZE);
    }

    @Override
    protected boolean filter(Method method) {
        return METHOD_NAME.equals(method.getName());
    }

    @Override
    protected Object intercept(Invocation<AviatorFunction> invocation, Object proxy) {
        Object[] args = invocation.args();
        Assert.isTrue(args.length >= NumberConstant.ONE);

        // 理论上，Env一定存在
        Env env = (Env) args[0];

        // 根据AviatorFunction的实现逻辑不同，这里 objects 参数个数不固定
        AviatorObject[] objects = Arrays.stream(ArrayUtil.sub(args, NumberConstant.ONE, args.length))
                .map(AviatorObject.class::cast)
                .toArray(AviatorObject[]::new);

        FunctionDefinition from = FunctionDefinition.from(this.definition);
        from.setInvocation(invocation);

        return this.withPlugin(from, env, objects);
    }

    @Override
    public Object handle(FunctionSession session) {
        Invocation<?> invocation = session.getDefinition().getInvocation();

        Object[] args = new Object[session.getObjects().length + NumberConstant.ONE];
        args[0] = session.getEnv();
        System.arraycopy(session.getObjects(), 0, args, 1, session.getObjects().length);

        return ReflectUtil.invoke(invocation.target(), invocation.method(), args);
    }

    @Override
    public List<UslPlugin> getPluginList() {
        return this.pluginList;
    }

    public FunctionDefinition getDefinition() {
        return this.definition;
    }
}
