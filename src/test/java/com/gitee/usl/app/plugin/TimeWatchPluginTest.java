package com.gitee.usl.app.plugin;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.UslRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.api.annotation.TimeWatchable;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
class TimeWatchPluginTest {

    @Test
    void test() {
        UslRunner runner = new UslRunner(UslRunner.defaultConfiguration()
                .configEngine()
                .scan(TimeWatchPluginTest.class)
                .finish());

        runner.start();

        runner.run(new Param().setScript("sleep()"));
    }


    @Func
    static class WatchableClass {

        @TimeWatchable(threshold = 1, unit = TimeUnit.SECONDS)
        @Func("sleep")
        public void timeoutFunc() {
            // 睡眠五秒
            ThreadUtil.sleep(1000 * 5);
        }
    }
}