package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.structure.Plugins;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * USL 函数实例
 *
 * @author hongda.li
 */
public class AnnotatedFunction extends AbstractVariadicFunction implements FunctionPluggable {
    private static final long serialVersionUID = 2613339911646206249L;
    private final transient Plugins plugins;
    private final transient FunctionDefinition definition;

    public AnnotatedFunction(FunctionDefinition definition) {
        this.definition = definition;
        this.plugins = new Plugins();
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        // 封装调用会话
        FunctionSession session = new FunctionSession((Env) env, args, this.definition);

        // 封装参数，但若后续存在参数绑定插件，则会重写此参数
        Object[] params;
        if (ArrayUtil.isEmpty(args)) {
            params = new Object[]{env};
        } else {
            List<Object> list = Stream.of(args).collect(Collectors.toList());
            list.add(NumberConstant.ZERO, env);
            params = list.toArray();
        }
        session.setInvocation(this.definition.methodMeta().toInvocation(params));

        // 基于插件来执行函数可以更好的动态扩展功能
        return this.withPlugin(session);
    }

    @Override
    public Object handle(final FunctionSession session) {
        return session.invocation().invoke();
    }

    @Override
    public String getName() {
        return Optional.ofNullable(this.definition).map(FunctionDefinition::name).orElse(null);
    }

    @Override
    public Plugins plugins() {
        return this.plugins;
    }

    public FunctionDefinition definition() {
        return definition;
    }
}
