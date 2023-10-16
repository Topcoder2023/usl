package com.gitee.usl.kernel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.*;

/**
 * @author hongda.li
 */
class JsEngineTest {

    @Test
    void test() throws ScriptException {
        // 获得javascript的脚本引擎
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");

        // 进行脚本编译
        String script = "function process(){\n" +
                "var a=10\n" +
                "var b=3\n" +
                "return a*b-c\n" +
                "}\n" +
                "process()";
        CompiledScript compiledScript = ((Compilable) scriptEngine).compile(script);

        // 绑定java的参数
        Bindings bindings = new SimpleBindings();
        bindings.put("c", 5);

        // 执行并打印结果
        Object result = compiledScript.eval(bindings);
        Assertions.assertEquals(25.0, result);
    }
}
