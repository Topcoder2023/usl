package com.manda.usl.kernel;

import com.manda.usl.kernel.view.Param;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class USLRunnerTest {

    @Test
    void run() {
        USLRunner runner = new USLRunner();

        runner.run();

        assertEquals(36.0d, runner.execute(new Param("test_sum(10, var1, 25.5)")
                .addContext("var1", 0.5)));
    }
}