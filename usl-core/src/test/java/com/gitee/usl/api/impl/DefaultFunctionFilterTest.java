package com.gitee.usl.api.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class DefaultFunctionFilterTest {

    @Test
    void allowedRegister() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .filter(new DefaultFunctionFilter("*_allowed")));
        runner.start();

        boolean expect = runner.functions()
                .stream()
                .anyMatch(function -> Objects.equals("func_allowed", function.name()))
                && runner.functions()
                .stream()
                .noneMatch(function -> Objects.equals("func_not_allowed", function.name()));
        assertTrue(expect);
    }

    @FunctionGroup
    static class TestFunction {

        @Function("func_allowed")
        void allowedFunction() {
        }

        @Function("func_not_allowed")
        void notAllowedFunction() {
        }
    }
}