package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author hongda.li
 */
class ParameterBinderPluginTest {

    @Test
    void test() {
        USLRunner runner = new USLRunner();
        runner.start();

        Param param = new Param();

        runner.run(param.setScript("test1()"));

        runner.run(param.setScript("test2(1, 5)"));

        runner.run(param.setScript("test3('hello')"));

        runner.run(param.setScript("test4(6, 7, 8, 9)"));

        runner.run(param.setScript("test5('hello')"));
    }

    @FunctionGroup
    static class InnerFunc {

        @Function
        void test1() {
            System.out.println("test1");
        }

        @Function
        void test2(int a, double b) {
            System.out.println(a + b);
        }

        @Function
        void test3(String a, Date b) {
            System.out.println(a + b);
        }

        @Function
        void test4(int a, double[] b) {
            System.out.println(a + b[0]);
        }

        @Function
        void test5(Env env, FunctionSession session, AviatorObject object) {
            System.out.println(session.getDefinition().getName() + object.getValue(env));
        }
    }

}