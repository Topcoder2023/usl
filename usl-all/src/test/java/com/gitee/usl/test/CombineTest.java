package com.gitee.usl.test;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Combine;
import com.gitee.usl.api.annotation.Engine;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.api.annotation.Param;
import com.gitee.usl.kernel.domain.Result;
import org.junit.jupiter.api.Test;

/**
 * @author hongda.li
 */
class CombineTest {

    @Test
    void test() {
        USLRunner runner = new USLRunner();
        runner.start();

        runner.run(new com.gitee.usl.kernel.domain.Param().setScript("random.chinese()"));

        Result<Object> run = runner.run(new com.gitee.usl.kernel.domain.Param().setScript("combine2(10)"));
        System.out.println(run);
    }


    @Func
    interface CombineFuncA {

        @Func({"combine", "combine2", "combine3"})
        @Combine("function process(){\n" +
                "var a=10\n" +
                "var b=3\n" +
                "return a*b-c\n" +
                "}\n" +
                "process()")
        @Engine
        int method(@Param("c") int var);
    }
}
