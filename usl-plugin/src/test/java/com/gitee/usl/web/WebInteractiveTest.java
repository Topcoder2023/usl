package com.gitee.usl.web;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.InteractiveMode;

/**
 * @author hongda.li
 */
class WebInteractiveTest {

    public static void main(String[] args) {
        new USLRunner().start(InteractiveMode.WEB);
    }

    @FunctionGroup
    static class TempFunc {

        @Function("sum")
        int sum(int a, int b) {
            return (int) (a + b);
        }
    }
}