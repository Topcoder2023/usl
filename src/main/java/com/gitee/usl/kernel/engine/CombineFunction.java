package com.gitee.usl.kernel.engine;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.UslRunner;
import com.gitee.usl.api.annotation.CombineFunc;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.infra.proxy.MethodInterceptor;
import com.gitee.usl.infra.structure.Plugins;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.plugin.Plugin;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hongda.li
 */
public class CombineFunction extends MethodInterceptor<AviatorFunction> implements FunctionPluggable {
    private final FunctionDefinition definition;
    private final String content;
    private final UslRunner runner;

    public CombineFunction(FunctionDefinition definition) {
        super(AviatorFunction.class);
        this.definition = definition;
        CombineFunc combineFunc = AnnotationUtil.getAnnotation(definition.methodMeta().method(), CombineFunc.class);
        this.content = combineFunc.content();
        this.runner = UslRunner.findRunnerByName(combineFunc.engineName());
    }

    @Override
    protected boolean filter(Method method) {
        return METHOD_NAME.equals(method.getName());
    }

    @Override
    protected Object intercept(Invocation<AviatorFunction> invocation, Object proxy) {
        return runner.run(new Param().setContent(this.content).setCached(true).setContent(null)).getData();
    }

    @Override
    public Object handle(FunctionSession session) {
        return null;
    }

    @Override
    public Plugins plugins() {
        return null;
    }
}
