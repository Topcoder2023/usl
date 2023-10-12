package com.gitee.usl.app.plugin;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.UslRunner;
import com.gitee.usl.api.annotation.Asynchronous;
import com.gitee.usl.api.annotation.Cacheable;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.domain.Param;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class CacheablePluginTest {
    @Test
    void test() {
        UslRunner runner = new UslRunner(UslRunner.defaultConfiguration()
                .configEngine()
                .scan(CacheablePluginTest.class)
                .finish());

        runner.start();

        runner.run(new Param().setScript("cache()"));
        runner.run(new Param().setScript("cache()"));

        runner.run(new Param().setScript("cache_native()"));
        runner.run(new Param().setScript("cache_native()"));
    }


    @Func
    static class AsyncClass {

        @Cacheable
        @Func("cache")
        public String waitFunc() {
            return "success";
        }
    }

    @Cacheable
    static class AsyncFunction extends AbstractVariadicFunction {

        @Override
        public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
            return new AviatorString("success");
        }

        @Override
        public String getName() {
            return "cache_native";
        }
    }
}