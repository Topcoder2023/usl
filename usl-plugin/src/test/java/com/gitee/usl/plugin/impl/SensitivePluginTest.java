package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.Sensitized;
import com.gitee.usl.plugin.impl.sensitive.SensitiveType;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
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

    @Func
    static class SensitiveClass {

        @Sensitized(SensitiveType.CHINESE_NAME)
        @Func("sensitive")
        public String sensitiveFunc() {
            return "测试";
        }
    }

    @Sensitized(SensitiveType.CHINESE_NAME)
    static class SensitiveFunction extends AbstractVariadicFunction {

        @Override
        public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
            return new AviatorString("测试");
        }

        @Override
        public String getName() {
            return "sensitive_native";
        }
    }
}