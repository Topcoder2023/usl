package com.googlecode.aviator.code;

import com.gitee.usl.grammar.ScriptEngine;
import com.googlecode.aviator.code.asm.ASMCodeGenerator.MethodMetaData;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.asm.GlobalClassLoader;
import com.googlecode.aviator.parser.Parser;
import com.googlecode.aviator.parser.VariableMeta;
import com.googlecode.aviator.runtime.FunctionArgument;
import com.googlecode.aviator.runtime.LambdaFunctionBootstrap;
import com.googlecode.aviator.utils.Env;

import java.util.*;

/**
 * @author hongda.li
 */
public abstract class BaseEvalCodeGenerator implements EvalCodeGenerator {

    protected final ScriptEngine instance;

    protected Map<String, VariableMeta> variables = Collections.emptyMap();
    protected LambdaGenerator lambdaGenerator;
    protected final GlobalClassLoader classLoader;

    protected Parser parser;
    protected ScriptKeyword symbolTable;
    protected CodeGenerator parentCodeGenerator;
    protected Map<String, LambdaFunctionBootstrap> lambdaBootstraps;
    protected final ArrayDeque<MethodMetaData> methodMetaDataStack = new ArrayDeque<>();
    protected List<FunctionArgument> funcsArgs;
    private int funcInvocationId = 0;
    protected final Env compileEnv;

    protected List<FunctionArgument> getFuncsArgs() {
        if (this.funcsArgs == null) {
            this.funcsArgs = new ArrayList<>();
        }
        return this.funcsArgs;
    }

    protected int getNextFuncInvocationId() {
        return this.funcInvocationId++;
    }

    @Override
    public void setParser(final Parser parser) {
        this.parser = parser;
        this.symbolTable = this.parser.getSymbolTable();
    }

    @Override
    public void setLambdaBootstraps(final Map<String, LambdaFunctionBootstrap> lambdaBootstraps) {
        this.lambdaBootstraps = lambdaBootstraps;
    }

    @Override
    public GlobalClassLoader getClassLoader() {
        return this.classLoader;
    }

    public BaseEvalCodeGenerator(final ScriptEngine instance,
                                 final GlobalClassLoader classLoader) {
        super();
        this.instance = instance;
        this.compileEnv = new Env();
        this.compileEnv.setInstance(this.instance);

        this.classLoader = classLoader;
    }

}
