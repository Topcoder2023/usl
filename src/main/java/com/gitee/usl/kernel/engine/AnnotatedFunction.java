package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.infra.structure.Plugins;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.io.Serial;
import java.util.Map;
import java.util.Optional;

/**
 * USL 函数实例
 *
 * @author hongda.li
 */
public class AnnotatedFunction extends AbstractVariadicFunction implements FunctionPluggable {
    @Serial
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
        Object[] params = ArrayUtil.append(new Object[]{env}, args);
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
