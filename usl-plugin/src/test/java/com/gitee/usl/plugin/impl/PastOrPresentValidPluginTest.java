package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.PastOrPresent;
import com.gitee.usl.plugin.enhancer.PastOrPresentValidEnhancer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 时间注解测试
 *
 * @author jiahao.song
 */
class PastOrPresentValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new PastOrPresentValidEnhancer()));

        Result result = runner.run(new Param("test10('2025-01-01T00:00')"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class PastOrPresentFunctionTest {

        @Function("test10")
        LocalDateTime testPastOrPresent(@PastOrPresent LocalDateTime dateTime) {
            return dateTime;
        }
    }
}