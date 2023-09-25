package com.gitee.usl;

import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.Param;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author hongda.li
 */
class UslRunnerTest {

    public static void main(String[] args) {
        UslRunner runner = new UslRunner(new UslConfiguration()
                .configEngine(eng -> eng.scan(UslRunnerTest.class)));

        Param param = new Param()
                .setContent("1 + 100 * 100 * 200 + math.log(var)")
                .setContext("var", 10.5);

        System.out.println(runner.run(param).getData());

        param.setContent("test()");

        System.out.println(runner.run(param).getData());

        param.setContent("native()");

        System.out.println(runner.run(param));
    }

    @Func
    public static class FuncTest {

        @Func({"测试", "test"})
        public String runTest() {
            return "success";
        }
    }

    public static class FuncTest2 extends AbstractFunction {

        @Override
        public String getName() {
            return "native";
        }

        @Override
        public AviatorObject call(Map<String, Object> env) {
            return new AviatorString("native success.");
        }
    }
}