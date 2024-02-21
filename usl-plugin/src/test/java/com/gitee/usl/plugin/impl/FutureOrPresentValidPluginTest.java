package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.FutureOrPresent;
import com.gitee.usl.plugin.enhancer.FutureOrPresentValidEnhancer;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 未来日期或现在日期注解测试
 * @author jingshu.zeng
 */
class FutureOrPresentValidPluginTest {

    @Test
    void validFutureDate() {
        // 创建一个未来日期
        long futureTime = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000; // 假设未来5天，以毫秒为单位

        // 创建USLRunner并启用调试模式
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new FutureOrPresentValidEnhancer()));

        // 调用函数并传入未来日期作为参数
        Result result = runner.run(new Param("validateFutureOrPresent(" + futureTime + ")"));

        // 验证结果为成功
        assertEquals(ResultCode.SUCCESS.code(), result.getCode());
    }


    @Test
    void validPastDate() {
        // 创建一个过去日期
        long pastTime = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000; // 减去五天，得到过去日期，以毫秒为单位


        // 创建USLRunner并启用调试模式
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new FutureOrPresentValidEnhancer()));

        // 调用函数并传入过去日期作为参数
        Result result = runner.run(new Param("validateFutureOrPresent(" + pastTime + ")"));

        // 验证结果为失败
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @Test
    void validPresentDate() {
        // 创建一个当前日期
        long currentTime = System.currentTimeMillis(); // 获取当前时间的毫秒级时间戳

        // 创建USLRunner并启用调试模式
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new FutureOrPresentValidEnhancer()));

        // 调用函数并传入当前日期作为参数
        Result result = runner.run(new Param("validateFutureOrPresent(" + currentTime + ")"));

        // 验证结果为成功
        assertEquals(ResultCode.SUCCESS.code(), result.getCode());
    }


    @FunctionGroup
    static class FutureOrPresentFunctionTest {
        @Function("validateFutureOrPresent")
        Date validateFutureOrPresent(@FutureOrPresent Date futureOrPresentDate) {
            return futureOrPresentDate;
        }

    }
}
