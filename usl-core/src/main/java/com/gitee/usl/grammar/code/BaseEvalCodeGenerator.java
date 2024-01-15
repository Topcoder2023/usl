package com.gitee.usl.grammar.code;

import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.ScriptClassLoader;
import com.gitee.usl.grammar.info.MethodMetaData;
import com.gitee.usl.grammar.parser.Parser;
import com.gitee.usl.grammar.parser.VariableMeta;
import com.gitee.usl.grammar.runtime.FunctionArgument;
import com.gitee.usl.grammar.runtime.LambdaFunctionBootstrap;
import com.gitee.usl.grammar.utils.Env;

import java.util.*;

/**
 * @author hongda.li
 */
public abstract class BaseEvalCodeGenerator implements CodeGenerator {

    protected final ScriptEngine instance;
    protected Map<String, VariableMeta> variables = Collections.emptyMap();
    protected LambdaGenerator lambdaGenerator;
    protected final ScriptClassLoader classLoader;

    protected Parser parser;
    protected ScriptKeyword symbolTable;
    protected CodeGenerator parentCodeGenerator;
    protected Map<String, LambdaFunctionBootstrap> lambdaBootstraps;
    protected final ArrayDeque<MethodMetaData> methodMetaDataStack = new ArrayDeque<>();
    protected List<FunctionArgument> functionArguments;
    private int funcInvocationId = 0;
    protected final Env compileEnv;

    protected List<FunctionArgument> getFunctionArguments() {
        if (this.functionArguments == null) {
            this.functionArguments = new ArrayList<>();
        }
        return this.functionArguments;
    }

    protected int getNextFuncInvocationId() {
        return this.funcInvocationId++;
    }

    @Override
    public void setParser(final Parser parser) {
        this.parser = parser;
        this.symbolTable = this.parser.getSymbolTable();
    }

    public BaseEvalCodeGenerator(final ScriptEngine instance,
                                 final ScriptClassLoader classLoader) {
        super();
        this.instance = instance;
        this.compileEnv = new Env();
        this.compileEnv.setInstance(this.instance);
        this.classLoader = classLoader;
    }

}
