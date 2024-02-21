package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.Future;
import com.gitee.usl.plugin.enhancer.FutureValidEnhancer;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 未来日期注解测试
 * @author jingshu.zeng
 */
class FutureValidPluginTest {

    @Test
    void validFutureDate() {
        // 创建一个未来日期
        Calendar futureCalendar = Calendar.getInstance();
        futureCalendar.add(Calendar.DATE, 5); // 假设未来5天
        Date futureDate = futureCalendar.getTime();

        // 创建USLRunner并启用调试模式
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new FutureValidEnhancer()));

        // 调用函数并传入未来日期作为参数
        Result result = runner.run(new Param("validateFuture(" + futureDate.getTime() + ")"));

        // 验证结果为成功
        assertEquals(ResultCode.SUCCESS.code(), result.getCode());
        // 验证返回值是否与传入的未来日期一致
        assertEquals(futureDate, result.getData());
    }


    @Test
    void validPastDate() {
        // 创建一个过去日期
        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.add(Calendar.DATE, -5); // 减去五天，得到过去日期
        Date pastDate = pastCalendar.getTime();

        // 创建USLRunner并启用调试模式
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new FutureValidEnhancer()));

        // 调用函数并传入过去日期作为参数
        Result result = runner.run(new Param("validateFuture(" + pastDate.getTime() + ")"));

        // 验证结果为失败
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }


    @FunctionGroup
    static class FutureFunctionTest {
        @Function("validateFuture")
        Date validateFuture(@Future Date futureDate) {
            return futureDate;
        }

    }
}
