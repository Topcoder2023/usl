package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.lang.reflect.Proxy;

/**
 * @author hongda.li
 */
public abstract class AbstractFunctionEnhancer implements FunctionEnhancer {
    @Override
    public void enhance(AviatorFunction function) {
        if (function instanceof AnnotatedFunction af) {
            this.enhanceAnnotatedFunction(af);
        }

        boolean isProxy = Proxy.isProxyClass(function.getClass());
        if (isProxy && Proxy.getInvocationHandler(function) instanceof NativeFunction nf) {
            this.enhanceNativeFunction(nf);
        }

        this.enhanceFunction(function);
    }

    protected void enhanceFunction(AviatorFunction func) {
    }

    protected void enhanceNativeFunction(NativeFunction nf) {
    }

    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
    }
}
