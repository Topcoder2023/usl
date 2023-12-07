package com.gitee.usl.plugin.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.Asynchronous;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hongda.li
 */
class AsyncPluginTest {
    @Test
    void test() {
        USLRunner runner = new USLRunner();

        runner.start();

        runner.run(new Param().setScript("wait()"));

        runner.run(new Param().setScript("wait_native()"));
    }


    @FunctionGroup
    static class AsyncClass {

        @Asynchronous
        @Function("wait")
        public void waitFunc() {
            // 睡眠六十秒
            ThreadUtil.sleep(1000 * 60);
        }
    }

    @Asynchronous
    static class AsyncFunction extends AbstractVariadicFunction {

        @Override
        public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
            // 睡眠六十秒
            ThreadUtil.sleep(1000 * 60);
            return AviatorNil.NIL;
        }

        @Override
        public String getName() {
            return "wait_native";
        }
    }
}