package com.gitee.usl;

import cn.hutool.core.util.TypeUtil;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.kernel.binder.Converter;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.Param;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author hongda.li
 */
class UslRunnerTest {

    public static void main(String[] args) {
        Type argument = TypeUtil.getTypeArgument(TestConverter.class);
        System.out.println(argument.getTypeName());

        UslRunner runner = new UslRunner(new UslConfiguration()
                .configEngine(eng -> eng.scan(UslRunnerTest.class)));
        runner.start();

        Param param = new Param()
                .setContent("1 + 100 * 100 * 200 + math.log(var)")
                .setContext("var", 10.5);

        System.out.println(runner.run(param).getData());

        param.setContent("test.a(1)");

        System.out.println(runner.run(param).getData());

        param.setContent("native(10.5)");

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