package com.gitee.usl.grammar.asm;

import com.gitee.usl.api.annotation.AsmMethod;
import com.gitee.usl.api.annotation.Description;
import com.googlecode.aviator.runtime.FunctionArgument;
import com.googlecode.aviator.runtime.function.LambdaFunction;
import com.googlecode.aviator.utils.Env;

import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@Description("脚本增强器")
public interface ScriptEnhancer {

    @AsmMethod
    @Description("生成匿名Lambda函数")
    default LambdaFunction newLambda(final Env env, final String name) {
        return null;
    }

    @Description("设置当前函数参数")
    default void setFunctionsArgs(final Map<Integer, List<FunctionArgument>> functionsArgs) {
    }

    @Description("设置上下文环境")
    default void setCompileEnv(final Env env) {
    }

}
