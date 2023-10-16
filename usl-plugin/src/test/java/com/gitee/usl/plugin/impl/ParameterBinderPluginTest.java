package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Func;
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

    @Func
    static class InnerFunc {

        @Func
        void test1() {
            System.out.println("test1");
        }

        @Func
        void test2(int a, double b) {
            System.out.println(a + b);
        }

        @Func
        void test3(String a, Date b) {
            System.out.println(a + b);
        }

        @Func
        void test4(int a, double[] b) {
            System.out.println(a + b[0]);
        }

        @Func
        void test5(Env env, FunctionSession session, AviatorObject object) {
            System.out.println(session.definition().name() + object.getValue(env));
        }
    }

}