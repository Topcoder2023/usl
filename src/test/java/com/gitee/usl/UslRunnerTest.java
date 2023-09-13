package com.gitee.usl;

import com.gitee.usl.kernel.configure.UslConfiguration;

/**
 * @author hongda.li
 */
class UslRunnerTest {

    public static void main(String[] args) {
        new UslRunner(new UslConfiguration()).run(null);
    }
}