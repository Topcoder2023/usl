package com.gitee.usl.infra.time;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.configure.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Description("定时任务")
@FunctionalInterface
public interface TimedTask {

    void doTask(Configuration configuration);

    default long initDelay() {
        return NumberConstant.ZERO;
    }

    default TimeUnit unit() {
        return TimeUnit.SECONDS;
    }

}
