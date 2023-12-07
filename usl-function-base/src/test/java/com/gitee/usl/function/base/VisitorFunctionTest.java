package com.gitee.usl.function.base;

import cn.hutool.core.map.MapUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.structure.Script;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class VisitorFunctionTest {
    static USLRunner runner;

    @BeforeAll
    static void init() {
        runner = new USLRunner();
        runner.start();
    }

    @Test
    void get() {
        Map<Object, Object> build = MapUtil.builder()
                .put("k", "v")
                .build();
        assertEquals("v", runner.run(new Param()
                        .setScript("get(obj, 'k')")
                        .addContext("obj", build))
                .getData());

        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals("c", runner.run(new Param()
                        .setScript("get(obj, '2')")
                        .addContext("obj", list))
                .getData());

        Object[] array = new Object[]{"i", "j", "k"};
        assertEquals("k", runner.run(new Param()
                        .setScript("get(obj, '2')")
                        .addContext("obj", array))
                .getData());

        USLRunner obj = new USLRunner("testName", USLRunner.defaultConfiguration());
        assertEquals("testName", runner.run(new Param()
                        .setScript("get(obj, 'name')")
                        .addContext("obj", obj))
                .getData());
    }

    @Test
    void set() {
        USLRunner obj = new USLRunner();
        assertEquals("testName2", runner.run(new Param()
                        .setScript("set(obj, 'name', 'testName2')")
                        .addContext("obj", obj))
                .getData());
        assertEquals("testName2", runner.run(new Param()
                        .setScript("get(obj, 'name')")
                        .addContext("obj", obj))
                .getData());
    }

    @Test
    void invoke() {
        assertEquals("test", runner.run(new Param()
                .setScript("invoke(var, 'getPath')")
                .addContext("var", new Script(runner, "test"))).getData());
    }
}