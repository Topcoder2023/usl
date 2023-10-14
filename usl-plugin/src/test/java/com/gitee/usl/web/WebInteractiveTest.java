package com.gitee.usl.web;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.enums.InteractiveMode;

/**
 * @author hongda.li
 */
class WebInteractiveTest {

    public static void main(String[] args) {
        new USLRunner().start(InteractiveMode.WEB);
    }

    @Func
    static class TempFunc {

        @Func("sum")
        int sum(int a, int b) {
            return (int) (a + b);
        }
    }
}