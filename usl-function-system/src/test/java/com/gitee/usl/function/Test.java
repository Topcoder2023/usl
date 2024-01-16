package com.gitee.usl.function;

import com.gitee.usl.Runner;
import com.gitee.usl.kernel.domain.ResourceParam;

/**
 * @author hongda.li
 */
class Test {
    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.start();

        System.out.println(runner.run(new ResourceParam("desktop.js")));
    }
}