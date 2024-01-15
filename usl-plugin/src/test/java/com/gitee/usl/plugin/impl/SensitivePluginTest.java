package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.Sensitized;
import com.gitee.usl.plugin.impl.sensitive.SensitiveType;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hongda.li
 */
class SensitivePluginTest {
    @Test
    void test() {
        USLRunner runner = new USLRunner();

        runner.start();

        runner.run(new Param().setScript("sensitive()"));

        runner.run(new Param().setScript("sensitive_native()"));
    }

    @FunctionGroup
    static class SensitiveClass {

        @Sensitized(SensitiveType.CHINESE_NAME)
        @Function("sensitive")
        public String sensitiveFunc() {
            return "测试";
        }
    }

    @Sensitized(SensitiveType.CHINESE_NAME)
    static class SensitiveFunction extends AbstractVariadicFunction {

        @Override
        public USLObject variadicCall(Map<String, Object> env, USLObject... args) {
            return new AviatorString("测试");
        }

        @Override
        public String getName() {
            return "sensitive_native";
        }
    }
}