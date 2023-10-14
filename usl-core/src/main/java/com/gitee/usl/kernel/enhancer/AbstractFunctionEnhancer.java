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
        boolean isProxy = Proxy.isProxyClass(function.getClass());

        // 增强注解函数
        if (function instanceof AnnotatedFunction) {
            this.enhanceAnnotatedFunction((AnnotatedFunction) function);
        }
        // 增强代理函数
        else if (isProxy && Proxy.getInvocationHandler(function) instanceof NativeFunction) {
            this.enhanceNativeFunction((NativeFunction) Proxy.getInvocationHandler(function));
        }
        // 增强其余函数
        else {
            this.enhanceFunction(function);
        }
    }

    protected void enhanceFunction(AviatorFunction func) {
    }

    protected void enhanceNativeFunction(NativeFunction nf) {
    }

    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
    }
}
