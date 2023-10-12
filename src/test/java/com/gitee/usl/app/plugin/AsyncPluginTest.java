package com.gitee.usl.app.plugin;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.UslRunner;
import com.gitee.usl.api.annotation.Asynchronous;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Test;


/**
 * @author hongda.li
 */
class AsyncPluginTest {
    @Test
    void test() {
        UslRunner runner = new UslRunner(UslRunner.defaultConfiguration()
                .configEngine()
                .scan(AsyncPluginTest.class)
                .finish());

        runner.start();

        runner.run(new Param().setScript("wait()"));
    }


    @Func
    static class AsyncClass {

        @Asynchronous
        @Func("wait")
        public void waitFunc() {
            // 睡眠六十秒
            ThreadUtil.sleep(1000 * 60);
        }
    }
}