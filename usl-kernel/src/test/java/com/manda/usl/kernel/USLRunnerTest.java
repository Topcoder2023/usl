package com.manda.usl.kernel;

import com.manda.usl.kernel.annotation.Func;
import com.manda.usl.kernel.annotation.FuncGroup;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.view.Param;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class USLRunnerTest {

    @Test
    void run() {
        USLRunner.run();

        USLRunner runner = BeanContext.findFirst(USLRunner.class);
        assertEquals(36.0d, runner.execute(new Param("test_sum(10, var1, 25.5)")
                .addContext("var1", 0.5)));
    }

    @Singleton
    @FuncGroup
    static class NumberFunctions {

        @Func("test_sum")
        double sum(int a, double b, float c) {
            return a + b + c;
        }
    }
}