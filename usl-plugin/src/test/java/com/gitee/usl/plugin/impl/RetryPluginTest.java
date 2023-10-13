package com.gitee.usl.plugin.impl;

import com.gitee.usl.UslRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.Retryable;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hongda.li
 */
class RetryPluginTest {
    @Test
    void test() {
        UslRunner runner = new UslRunner();

        runner.start();

        runner.run(new Param().setScript("retry()"));

        runner.run(new Param().setScript("retry_native()"));
    }


    @Func
    static class RetryClass {

        @Retryable
        @Func("retry")
        public String retryFunc() {
            throw new NullPointerException("empty");
        }
    }

    @Retryable
    static class RetryFunction extends AbstractVariadicFunction {

        @Override
        public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
            throw new NullPointerException("empty");
        }

        @Override
        public String getName() {
            return "retry_native";
        }
    }
}