package com.gitee.usl.plugin.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.TimeWatchable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
class TimeWatchPluginTest {

    @Test
    void test() {
        USLRunner runner = new USLRunner();

        runner.start();

        runner.run(new Param().setScript("sleep()"));
    }


    @FunctionGroup
    static class WatchableClass {

        @TimeWatchable(threshold = 1, unit = TimeUnit.SECONDS)
        @Function("sleep")
        public void timeoutFunc() {
            // 睡眠五秒
            ThreadUtil.sleep(1000 * 5);
        }
    }
}