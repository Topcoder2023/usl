package com.gitee.usl.api.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class UslEnvLoaderTest {
    static UslEnvLoader envLoader;

    @BeforeAll
    public static void before() {
        envLoader = UslEnvLoader.getInstance();
    }

    @Test
    void fetch() {
        assertNull(envLoader.fetch("name"));

        System.setProperty("name", "hongda.li");
        assertEquals("hongda.li", envLoader.fetch("name"));
    }
}