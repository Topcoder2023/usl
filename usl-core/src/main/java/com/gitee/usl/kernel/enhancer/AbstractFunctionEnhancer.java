package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.lang.reflect.Proxy;

/**
 * @author hongda.li
 */
@Description("使用模板模式构建函数增强逻辑")
public abstract class AbstractFunctionEnhancer implements FunctionEnhancer {
    @Override
    public void enhance(AviatorFunction function) {

        @Description("代理类标识")
        boolean isProxy = Proxy.isProxyClass(function.getClass());

        if (function instanceof AnnotatedFunction) {
            this.enhanceAnnotatedFunction((AnnotatedFunction) function);
        } else if (isProxy && Proxy.getInvocationHandler(function) instanceof NativeFunction) {
            this.enhanceNativeFunction((NativeFunction) Proxy.getInvocationHandler(function));
        } else {
            this.enhanceFunction(function);
        }
    }

    @Description("增强普通函数")
    protected void enhanceFunction(AviatorFunction func) {
    }

    @Description("增强代理函数")
    protected void enhanceNativeFunction(NativeFunction nf) {
    }

    @Description("增强注解函数")
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
    }

}
