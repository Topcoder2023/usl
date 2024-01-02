package com.googlecode.aviator.code;

import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.code.asm.ASMCodeGenerator.MethodMetaData;
import com.googlecode.aviator.lexer.SymbolTable;
import com.googlecode.aviator.parser.AviatorClassLoader;
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

  protected final AviatorEvaluatorInstance instance;

  protected Map<String, VariableMeta> variables = Collections.emptyMap();
  protected final String sourceFile = null;
  protected LambdaGenerator lambdaGenerator;
  protected final AviatorClassLoader classLoader;

  protected Parser parser;
  protected SymbolTable symbolTable;
  protected CodeGenerator parentCodeGenerator;
  protected Map<String, LambdaFunctionBootstrap> lambdaBootstraps;
  protected final ArrayDeque<MethodMetaData> methodMetaDataStack = new ArrayDeque<>();
  protected Map<Integer, List<FunctionArgument>> funcsArgs;
  private int funcInvocationId = 0;
  protected final Env compileEnv;

  protected Map<Integer, List<FunctionArgument>> getFuncsArgs() {
    if (this.funcsArgs == null) {
      this.funcsArgs = new HashMap<>();
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
  public AviatorClassLoader getClassLoader() {
    return this.classLoader;
  }

  public BaseEvalCodeGenerator(final AviatorEvaluatorInstance instance,
      final AviatorClassLoader classLoader) {
    super();
    this.instance = instance;
    this.compileEnv = new Env();
    this.compileEnv.setInstance(this.instance);

    this.classLoader = classLoader;
  }

}
