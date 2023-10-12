package com.gitee.usl;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.binder.Converter;
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
        UslRunner runner = new UslRunner(UslRunner.defaultConfiguration()
                .configEngine()
                .scan(UslRunnerTest.class)
                .finish());
        runner.start();

        Param param = new Param().setScript("str.size('hello')");

        System.out.println(runner.run(param));

        System.out.println(runner.run(param));

        ThreadUtil.sleep(6000);

        System.out.println(runner.run(param));
    }

    public static class TestConverter implements Converter<Long> {

        @Override
        public Long convert(Object sourceValue) {
            return Long.parseLong(String.valueOf(sourceValue));
        }
    }

    @Func
    public static class FuncTest {

        @Func({"测试", "test.a"})
        public String runTest(Integer a) {
            return "success : " + a;
        }
    }

    public static class FuncTest2 extends AbstractFunction {

        @Override
        public String getName() {
            return "native";
        }

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject object) {
            return new AviatorString("native success." + object.getValue(env));
        }
    }
}