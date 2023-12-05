package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class ListFunctionTest {
    static USLRunner runner;

    @BeforeAll
    static void before() {
        runner = new USLRunner();
        runner.start();
    }

    @Test
    void list() {
        Object data = runner.run(new Param().setScript("list('a', 'b', 'c', 'd')")).getData();
        assertInstanceOf(List.class, data);

        runner.run(new Param().setScript("for i in list {\n" +
                " logger.info(i);\n" +
                "}").addContext("list", Arrays.asList("a", "b", "c", "d")));
    }

    @Test
    void copyList() {
    }

    @Test
    void getList() {
    }

    @Test
    void addList() {
    }

    @Test
    void addAllList() {
    }

    @Test
    void addToList() {
    }

    @Test
    void remove() {
    }

    @Test
    void sort() {
    }

    @Test
    void sortBy() {
        String script = "fn com(a, b) {" +
                "return -1;" +
                "};" +
                "\n" +
                "return list.sortBy(list('a', 'b', 'c', 'd'), com);";
        assertDoesNotThrow(() -> runner.run(new Param().setScript(script)));
    }

    @Test
    void resort() {
    }

    @Test
    void resortBy() {
    }
}