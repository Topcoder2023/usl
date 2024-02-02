package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.grammar.runtime.type._Function;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.isProxyClass;

/**
 * 使用模板模式构建函数增强逻辑
 *
 * @author hongda.li
 */
public abstract class AbstractFunctionEnhancer implements FunctionEnhancer {
    @Override
    public void enhance(_Function function) {

        this.enhanceFunction(function);

        if (function instanceof AnnotatedFunction) {
            this.enhanceAnnotatedFunction((AnnotatedFunction) function);
        }

        if (function instanceof FunctionPluggable fp) {
            this.enhancePluggable(fp);
        }

        if (isProxyClass(function.getClass()) && getInvocationHandler(function) instanceof NativeFunction) {
            this.enhanceNativeFunction((NativeFunction) getInvocationHandler(function));
        }
    }

    @Description("增强普通函数")
    protected void enhanceFunction(_Function func) {
    }

    @Description("增强插件函数")
    protected void enhancePluggable(FunctionPluggable fp) {
    }

    @Description("增强代理函数")
    protected void enhanceNativeFunction(NativeFunction nf) {
    }

    @Description("增强注解函数")
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
    }

}
