package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.structure.Plugins;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.Map;
import java.util.Optional;

/**
 * USL 函数实例
 *
 * @author hongda.li
 */
public class AnnotatedFunction extends AbstractVariadicFunction implements FunctionPluggable {
    private final transient Plugins plugins;
    private final transient FunctionDefinition definition;

    public AnnotatedFunction(FunctionDefinition definition) {
        this.definition = definition;
        this.plugins = new Plugins();
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        // 基于插件来执行函数可以更好的动态扩展功能
        return this.withPlugin(new FunctionSession((Env) env, args, this.definition));
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
