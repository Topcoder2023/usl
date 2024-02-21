package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jingshu.zeng
 */
class NumberFunctionTest {

    static USLRunner runner = new USLRunner();

    @Test
    void abs() {
        Object data = runner.run(new Param("abs(-10.5)")).getData();
        assertEquals(10.5, data);
    }

    @Test
    void ceil() {
        Object data = runner.run(new Param("ceil(10.3)")).getData();
        assertEquals(11.0, data);
    }

    @Test
    void max() {
        Object data = runner.run(new Param("max(10.5, 20.7, 15.2)")).getData();
        assertEquals(20.7, data);
    }

    @Test
    void floor() {
        Object data = runner.run(new Param("floor(10.9)")).getData();
        assertEquals(10.0, data);
    }

    @Test
    void min() {
        Object data = runner.run(new Param("min(10.5, 20.7, 15.2)")).getData();
        assertEquals(10.5, data);
    }

    @Test
    void sqrt() {
        Object data = runner.run(new Param("sqrt(25.0)")).getData();
        assertEquals(5.0, data);
    }

    @Test
    void round() {
        Object data = runner.run(new Param("round(10.5678,2)")).getData();
        assertEquals(10.57, data);
    }


}
