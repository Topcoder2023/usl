package com.gitee.usl;

import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.UslParam;

/**
 * @author hongda.li
 */
class UslRunnerTest {

    public static void main(String[] args) {
        UslRunner runner = new UslRunner(new UslConfiguration()
                .configEngine(eng -> eng.scan(UslRunnerTest.class)));

        UslParam param = new UslParam()
                .setContent("1 + 100 * 100 * 200 + math.log(var)")
                .setContext("var", 10.5);

        System.out.println(runner.run(param).getData());

        param.setContent("test()");

        System.out.println(runner.run(param).getData());
    }

    @Func
    public static class FuncTest {

        @Func({"测试", "test"})
        public String runTest() {
            return "success";
        }
    }
}