package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.UslInvocation;
import com.gitee.usl.kernel.plugin.UslPlugin;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * USL 函数实例
 *
 * @author hongda.li
 */
public class UslFunction extends AbstractVariadicFunction implements UslFunctionPluggable {
    private final transient List<UslPlugin> pluginList;
    private final transient UslFunctionDefinition definition;

    public UslFunction(UslFunctionDefinition definition) {
        this.definition = definition;
        this.pluginList = new ArrayList<>(NumberConstant.COMMON_SIZE);
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        // 基于插件来执行函数可以更好的动态扩展功能
        return this.withPlugin(env, args);
    }

    @Override
    public Object handle(final UslFunctionSession session) {
        UslInvocation<?> invocation = definition.getInvocation();

        return ReflectUtil.invoke(invocation.target(), invocation.method());
    }

    @Override
    public String getName() {
        return this.definition.getName();
    }

    @Override
    public List<UslPlugin> getPluginList() {
        return this.pluginList;
    }

    @Override
    public UslFunctionDefinition getDefinition() {
        return definition;
    }
}
