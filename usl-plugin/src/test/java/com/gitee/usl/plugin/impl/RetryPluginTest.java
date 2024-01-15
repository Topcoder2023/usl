package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.Retryable;
import com.gitee.usl.grammar.runtime.function.AbstractVariadicFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hongda.li
 */
class RetryPluginTest {
    @Test
    void test() {
        USLRunner runner = new USLRunner();

        runner.start();

        runner.run(new Param().setScript("retry()"));

        runner.run(new Param().setScript("retry_native()"));
    }


    @FunctionGroup
    static class RetryClass {

        @Retryable
        @Function("retry")
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