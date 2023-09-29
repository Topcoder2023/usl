package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.kernel.plugin.UslPlugin;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * USL 函数实例
 *
 * @author hongda.li
 */
public class AnnotatedFunction extends AbstractVariadicFunction implements UslFunctionPluggable {
    private final transient List<UslPlugin> pluginList;
    private final transient FunctionDefinition definition;

    public AnnotatedFunction(FunctionDefinition definition) {
        this.definition = definition;
        this.pluginList = new ArrayList<>(NumberConstant.COMMON_SIZE);
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        // 基于插件来执行函数可以更好的动态扩展功能
        return this.withPlugin(FunctionDefinition.from(this.definition), env, args);
    }

    @Override
    public Object handle(final FunctionSession session) {
        Invocation<?> invocation = definition.getInvocation();

        return ReflectUtil.invoke(invocation.target(), invocation.method(), invocation.args());
    }

    @Override
    public String getName() {
        return Optional.ofNullable(this.definition).map(FunctionDefinition::getName).orElse(null);
    }

    @Override
    public List<UslPlugin> plugins() {
        return this.pluginList;
    }

    public FunctionDefinition definition() {
        return definition;
    }
}
