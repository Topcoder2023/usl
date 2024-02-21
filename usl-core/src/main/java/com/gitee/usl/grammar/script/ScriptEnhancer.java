package com.gitee.usl.grammar.script;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.runtime.FunctionArgument;
import com.gitee.usl.grammar.runtime.function.LambdaFunction;
import com.gitee.usl.grammar.utils.Env;

import java.util.List;

/**
 * @author hongda.li
 */
@Description("脚本增强器")
public interface ScriptEnhancer {

    @Description("生成匿名Lambda函数")
    default LambdaFunction newLambda(final Env env, final String name) {
        return null;
    }

    @Description("设置当前函数参数")
    default void setFunctionsArgs(final List<FunctionArgument> functionsArgs) {
    }

    @Description("设置上下文环境")
    default void setCompileEnv(final Env env) {
    }

}
