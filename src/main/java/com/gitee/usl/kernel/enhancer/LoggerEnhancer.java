package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.app.plugin.LoggerPlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.engine.FunctionEnhancer;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.lang.reflect.Proxy;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE - 10)
@AutoService(FunctionEnhancer.class)
public class LoggerEnhancer implements FunctionEnhancer {
    @Override
    public void enhance(AviatorFunction function) {
        if (function instanceof AnnotatedFunction af) {
            af.plugins().install(new LoggerPlugin());
        }

        boolean isProxy = Proxy.isProxyClass(function.getClass());
        if (isProxy && Proxy.getInvocationHandler(function) instanceof NativeFunction nf) {
            nf.plugins().install(new LoggerPlugin());
        }
    }
}
