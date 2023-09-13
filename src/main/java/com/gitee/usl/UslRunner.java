package com.gitee.usl;

import cn.hutool.core.date.StopWatch;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
public class UslRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(UslRunner.class);
    private final StopWatch watch;

    public UslRunner() {
        watch = new StopWatch();
    }

    public static UslRunner build() {
        UslRunner runner = new UslRunner();

        runner.watch.start();

        LOGGER.info("USL Starting...");

        // 获取所有初始化器，并依次执行初始化动作
        SpiServiceUtil.loadSortedService(Initializer.class).forEach(Initializer::doInit);

        LOGGER.info("USL started finish. Total time cost {} seconds", runner.watch.getTotalTimeSeconds());

        return runner;
    }

    public static void main(String[] args) {
        UslRunner.build().getConfig();
    }

    public StopWatch getWatch() {
        return watch;
    }

    public UslConfiguration getConfig() {
        return null;
    }
}
