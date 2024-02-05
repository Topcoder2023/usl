package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDFunctionTest {

    static USLRunner runner = new USLRunner();

    @Test
    void id_uuid() {
        Object data = runner.run(new Param("id_uuid()")).getData();
        assertInstanceOf(String.class, data);
    }

    @Test
    void id_uuid_simple() {
        Object data = runner.run(new Param("id_uuid_simple()")).getData();
        assertInstanceOf(String.class, data);
    }

    @Test
    void id_snow() {
        Object data = runner.run(new Param("id_snow()")).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void base64_encode() {
        String input = "Hello, World!";
        Object data = runner.run(new Param(String.format("base64_encode('%s')", input))).getData();
        assertInstanceOf(String.class, data);
        assertEquals("SGVsbG8sIFdvcmxkIQ==", data);
    }

    @Test
    void base64_decode() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        Object data = runner.run(new Param(String.format("base64_decode('%s')", input))).getData();
        assertInstanceOf(String.class, data);
        assertEquals("Hello, World!", data);
    }

    @Test
    void rot_encode() {
        String input = "Hello, World!";
        Object data = runner.run(new Param(String.format("rot_encode('%s')", input))).getData();
        assertInstanceOf(String.class, data);
        assertEquals("Uryyb, Jbeyq!", data);
    }

    @Test
    void rot_decode() {
        String input = "Uryyb, Jbeyq!";
        Object data = runner.run(new Param(String.format("rot_decode('%s')", input))).getData();
        assertInstanceOf(String.class, data);
        assertEquals("Hello, World!", data);
    }
}
