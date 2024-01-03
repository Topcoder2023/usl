package com.googlecode.aviator.code;

import com.gitee.usl.api.annotation.Description;
import com.googlecode.aviator.lexer.token.Token;
import com.gitee.usl.grammar.asm.GlobalClassLoader;
import com.googlecode.aviator.parser.VariableMeta;
import com.googlecode.aviator.runtime.LambdaFunctionBootstrap;

import java.util.Map;
import java.util.Set;

/**
 * @author hongda.li
 */
@Description("以运行期优先的字节码生成器")
public interface EvalCodeGenerator extends CodeGenerator {
    void start();

    void initVariables(final Map<String, VariableMeta> vars);

    void initConstants(final Set<Token<?>> constants);

    void initMethods(final Map<String, Integer> methods);

    void setLambdaBootstraps(final Map<String, LambdaFunctionBootstrap> lambdaBootstraps);

    GlobalClassLoader getClassLoader();

    void genNewLambdaCode(final LambdaFunctionBootstrap bootstrap);

}
