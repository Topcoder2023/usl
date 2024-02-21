package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.domain.Param;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * List 函数组单元测试
 *
 * @author hongda.li
 */
class ListFunctionTest {

    static USLRunner runner = new USLRunner();

    @Test
    void list() {
        Object data = runner.run(new Param("list()")).getData();
        assertInstanceOf(ArrayList.class, data);
    }

    @Test
    void of() {
        Object data = runner.run(new Param("list_of(1, 2, 3, x)").addContext("x", 4L)).getData();
        assertInstanceOf(ArrayList.class, data);
        ((List<?>) data).forEach(o -> assertInstanceOf(Long.class, o));
    }

    @Test
    void from() {
    }

    @Test
    void get() {
    }

    @Test
    void set() {
    }

    @Test
    void add() {
    }

    @Test
    void size() {
    }

    @Test
    void addAll() {
    }

    @Test
    void addTo() {
    }

    @Test
    void remove() {
    }

    @Test
    void removeIf() {
    }

    @Test
    void clear() {
    }

    @Test
    void indexOf() {
    }

    @Test
    void lastIndexOf() {
    }

    @Test
    void sort() {
    }

    @Test
    void resort() {
    }

    @Test
    void sortBy() {
    }

    @Test
    void resortBy() {
    }

    @Test
    void sub() {
    }

    @Test
    void filter() {
    }

    @Test
    void union() {
    }

    @Test
    void unionAll() {
    }

    @Test
    void unionDistinct() {
    }

    @Test
    void intersection() {
    }

    @Test
    void intersectionDistinct() {
    }

    @Test
    void disjunction() {
    }

    @Test
    void containsAny() {
    }

    @Test
    void containsAll() {
    }

    @Test
    void contains() {
    }

    @Test
    void distinct() {
    }

    @Test
    void join() {
    }

    @Test
    void allMatch() {
    }

    @Test
    void anyMatch() {
    }

    @Test
    void toMap() {
    }

    @Test
    void foreach() {
    }

    @Test
    void convert() {
    }

    @Test
    void group() {
    }
}