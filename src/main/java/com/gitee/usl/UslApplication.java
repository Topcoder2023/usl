package com.gitee.usl;

import com.gitee.usl.infra.thread.UslExecutor;
import com.gitee.usl.infra.time.TimedTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
public class UslApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(UslApplication.class);

    public static void main(String[] args) {
       LOGGER.info("USL Starting...");

        UslExecutor.submit(() -> LOGGER.info("USL started."));

       new TimedTaskManager().doInit();
    }
}
