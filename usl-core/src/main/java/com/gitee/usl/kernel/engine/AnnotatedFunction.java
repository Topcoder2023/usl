package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.api.Definable;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.structure.Plugins;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.*;

/**
 * @author hongda.li
 */
@Description("基于注解的函数")
public class AnnotatedFunction extends AbstractVariadicFunction
        implements FunctionPluggable, Definable {

    @Description("序列号")
    private static final long serialVersionUID = 2613339911646206249L;

    @Description("插件链")
    private final transient Plugins plugins = new Plugins();

    @Description("函数定义")
    private final transient FunctionDefinition definition;

    public AnnotatedFunction(FunctionDefinition definition) {
        this.definition = definition;
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {

        @Description("函数调用会话")
        FunctionSession session = new FunctionSession((Env) env, args, this.definition);

        @Description("原始参数列表")
        IntWrapper wrapper = new IntWrapper(ArrayUtil.isEmpty(args) ? 1 : args.length + 1);
        Object[] params = new Object[wrapper.get()];
        params[NumberConstant.ZERO] = env;
        while (wrapper.get() != 1) {
            params[params.length - wrapper.get() + 1] = args[params.length - wrapper.get()];
            wrapper.decrement();
        }

        session.setHandler(this::handle);
        session.setInvocation(this.definition.getMethodMeta().toInvocation(params));

        return this.withPlugin(session);
    }

    @Override
    public Object handle(final FunctionSession session) {
        return session.getInvocation().invoke();
    }

    @Override
    public String getName() {
        return Optional.ofNullable(this.definition).map(FunctionDefinition::getName).orElse(null);
    }

    @Override
    public Plugins plugins() {
        return this.plugins;
    }

    @Override
    public FunctionDefinition definition() {
        return definition;
    }

}
