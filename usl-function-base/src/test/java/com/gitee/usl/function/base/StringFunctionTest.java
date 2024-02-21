package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jingshu.zeng
 */

class StringFunctionTest {
    static USLRunner runner = new USLRunner();

    @Test
    void contains() {
        Object data = runner.run(new Param("contains('Hello World', 'World')")).getData();
        assertInstanceOf(Boolean.class, data);
        assertEquals(true, data);
    }


    @Test
    void starts_with() {
        Object data = runner.run(new Param("starts_with('Hello World', 'Hello')")).getData();
        assertInstanceOf(Boolean.class, data);
        assertEquals(true, data);
    }

    @Test
    void ends_with() {
        Object data = runner.run(new Param("ends_with('Hello World', 'World')")).getData();
        assertInstanceOf(Boolean.class, data);
        assertEquals(true, data);
    }

    @Test
    void index_of() {
        Object data = runner.run(new Param("index_of('Hello World', 'e')")).getData();
        assertEquals(1L, data);
    }

    @Test
    void replace() {
        Object data = runner.run(new Param("replace('Hello World', 'World', 'Universe')")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("Hello Universe", data);
    }

    @Test
    void substring() {
        Object data = runner.run(new Param("substring('Hello World', 6, 11)")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("World", data);
    }

    @Test
    void length() {
        Object data = runner.run(new Param("length('Hello')")).getData();
        assertEquals(5L, data);
    }




    @Test
    void to_lower_case() {
        Object data = runner.run(new Param("to_lower_case('Hello World')")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("hello world", data);
    }

    @Test
    void regex() {
        Object data = runner.run(new Param("regex('Hello World', '.*World.*')")).getData();
        assertEquals(1L, data);
    }

    @Test
    void pinyin() {
        Object data = runner.run(new Param("pinyin('你好123')")).getData();
        assertEquals("nihao123", data);
    }

    @Test
    void trim() {
        Object data = runner.run(new Param("trim('  Hello World  ')")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("Hello World", data);
    }

    @Test
    void trim_start() {
        Object data = runner.run(new Param("trim_start('  Hello World')")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("Hello World", data);
    }

    @Test
    void trim_end() {
        Object data = runner.run(new Param("trim_end('Hello World  ')")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("Hello World", data);
    }

    @Test
    void url_encode() {
        Object data = runner.run(new Param("url_encode('http://www.example.com/?name=John')")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("http%3A%2F%2Fwww.example.com%2F%3Fname%3DJohn", data);
    }

    @Test
    void url_decode() {
        Object data = runner.run(new Param("url_decode('http%3A%2F%2Fwww.example.com%2F%3Fname%3DJohn')")).getData();
        assertInstanceOf(String.class, data);
        assertEquals("http://www.example.com/?name=John", data);
    }
}