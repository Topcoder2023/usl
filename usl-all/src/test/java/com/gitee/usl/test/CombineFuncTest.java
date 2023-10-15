package com.gitee.usl.test;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.CombineFunc;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.api.annotation.Param;
import com.gitee.usl.kernel.domain.Result;
import org.junit.jupiter.api.Test;

/**
 * @author hongda.li
 */
class CombineFuncTest {

    @Test
    void test() {
        USLRunner runner = new USLRunner();
        runner.start();
        Result<Object> run = runner.run(new com.gitee.usl.kernel.domain.Param().setScript("combine(4, 6)"));
        System.out.println(run);
    }


    @Func
    interface CombineFuncA {

        @Func("combine")
        @CombineFunc("(var1 + var2) * 100")
        int method(@Param("var1") int a, @Param("var2") int b);
    }
}
