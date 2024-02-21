package com.gitee.usl.function;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.ResourceParam;

/**
 * @author hongda.li
 */
class Test {
    public static void main(String[] args) {
        USLRunner runner = new USLRunner();
        runner.start();

        System.out.println(runner.run(new ResourceParam("desktop.js")));
    }
}