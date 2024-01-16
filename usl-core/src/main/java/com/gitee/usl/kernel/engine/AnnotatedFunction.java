package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.api.Overloaded;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.structure.Plugins;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;

import java.util.*;

/**
 * @author hongda.li
 */
@Description("基于注解的函数")
public class AnnotatedFunction extends BasicFunction implements FunctionPluggable, Overloaded<AnnotatedFunction> {

    @Description("插件链")
    private final transient Plugins plugins = new Plugins();

    @Description("函数定义")
    private final transient FunctionDefinition definition;

    @Description("函数重载")
    private List<AnnotatedFunction> overloadList;

    public AnnotatedFunction(FunctionDefinition definition) {
        this.definition = definition;
    }

    @Override
    public _Object execute(Env env, _Object[] arguments) {

        @Description("函数实参数量")
        int actualArgsLength = ArrayUtil.length(arguments);

        @Description("函数重载转发标识")
        boolean dispatch = overloadList != null && definition.getArgsLength() != actualArgsLength;

        if (dispatch) {
            AnnotatedFunction overload = overloadList.stream()
                    .filter(af -> af.definition.getArgsLength() == actualArgsLength)
                    .findFirst()
                    .orElse(null);
            if (overload != null) {
                return overload.execute(env, arguments);
            }
        }

        @Description("函数调用会话")
        FunctionSession session = new FunctionSession(env, arguments, this.definition);

        @Description("原始参数列表")
        IntWrapper wrapper = new IntWrapper(ArrayUtil.isEmpty(arguments) ? 1 : arguments.length + 1);
        Object[] params = new Object[wrapper.get()];
        params[NumberConstant.ZERO] = env;
        while (wrapper.get() != 1) {
            params[params.length - wrapper.get() + 1] = arguments[params.length - wrapper.get()];
            wrapper.decrement();
        }

        session.setHandler(this::handle);
        session.setInvocation(this.definition.getMethodMeta().toInvocation(params));

        return this.withPlugin(session);
    }

    @Override
    @Description("执行实际逻辑")
    public Object handle(final FunctionSession session) {
        return session.getInvocation().invoke();
    }

    @Override
    @Description("获取函数名称")
    public String name() {
        return Optional.ofNullable(this.definition).map(FunctionDefinition::getName).orElse(null);
    }

    @Override
    @Description("获取函数插件链")
    public Plugins plugins() {
        return this.plugins;
    }

    @Override
    @Description("获取函数定义")
    public FunctionDefinition definition() {
        return definition;
    }

    @Override
    @Description("添加重载实现")
    public void addOverloadImpl(Overloaded<?> impl) {
        if (impl instanceof AnnotatedFunction) {
            if (overloadList == null) {
                overloadList = new ArrayList<>(NumberConstant.ONE);
            }
            overloadList.add((AnnotatedFunction) impl);
        }
    }

    @Override
    @Description("获取所有重载实现")
    public List<AnnotatedFunction> allOverloadImpl() {
        return overloadList;
    }
}
