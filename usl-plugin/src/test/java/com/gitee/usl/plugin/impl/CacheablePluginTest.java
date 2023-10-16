package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.Cacheable;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hongda.li
 */
class CacheablePluginTest {
    @Test
    void test() {
        USLRunner runner = new USLRunner();

        runner.start();

        runner.run(new Param().setScript("cache(5)"));
        runner.run(new Param().setScript("cache(5)"));

        runner.run(new Param().setScript("cache_native(10)"));
        runner.run(new Param().setScript("cache_native(10)"));
    }


    @Func
    static class CacheClass {

        @Cacheable
        @Func("cache")
        public String waitFunc(int id) {
            return "success : " + id;
        }
    }

    @Cacheable
    static class CacheFunction extends AbstractVariadicFunction {

        @Override
        public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
            return new AviatorString("success : " + args[0].getValue(env));
        }

        @Override
        public String getName() {
            return "cache_native";
        }
    }
}