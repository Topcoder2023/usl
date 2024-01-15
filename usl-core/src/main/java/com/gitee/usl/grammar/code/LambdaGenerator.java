package com.gitee.usl.grammar.code;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.ScriptClassLoader;
import com.gitee.usl.grammar.parser.Parser;
import com.gitee.usl.grammar.parser.ScopeInfo;
import com.gitee.usl.grammar.runtime.FunctionParam;
import com.gitee.usl.grammar.runtime.LambdaFunctionBootstrap;
import com.gitee.usl.infra.constant.AsmConstants;

/**
 * Lambda function generator
 *
 * @author dennis
 *
 */
public class LambdaGenerator implements CodeGenerator {
    // private final ClassWriter classWriter;
    private final List<FunctionParam> params;
    private final CodeGenerator codeGenerator;
    private final CodeGenerator parentCodeGenerator;
    // private final AviatorClassLoader classLoader;
    // private final AviatorEvaluatorInstance instance;
    private final String className;
    private static final AtomicLong LAMBDA_COUNTER = new AtomicLong();
    // private MethodVisitor mv;
    private ScopeInfo scopeInfo;
    private final boolean newLexicalScope;
    private final boolean inheritEnv;

    public LambdaGenerator(final ScriptEngine instance,
                           final CodeGenerator parentCodeGenerator, final Parser parser,
                           final ScriptClassLoader classLoader, final boolean newLexicalScope,
                           final boolean inheritEnv) {
        this.params = new ArrayList<>();
        // this.instance = instance;
        this.parentCodeGenerator = parentCodeGenerator;
        this.codeGenerator = instance.codeGenerator(classLoader);
        this.codeGenerator.setParser(parser);
        // this.classLoader = classLoader;
        this.newLexicalScope = newLexicalScope;
        this.inheritEnv = inheritEnv;
        // Generate lambda class name
        this.className = "Script"
                + DateUtil.format(new DateTime(), AsmConstants.DATETIME_FORMAT)
                + StrPool.UNDERLINE
                + LAMBDA_COUNTER.getAndIncrement();
        // Auto compute frames
        // this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        // visitClass();
        // makeConstructor();
        // makeGetName();
    }


    public ScopeInfo getScopeInfo() {
        return this.scopeInfo;
    }


    public void setScopeInfo(final ScopeInfo scopeInfo) {
        this.scopeInfo = scopeInfo;
    }


    @Override
    public void setParser(final Parser parser) {
        this.codeGenerator.setParser(parser);
    }



    public LambdaFunctionBootstrap getLmabdaBootstrap() {
        Script expression = getResult(!this.newLexicalScope);
        return new LambdaFunctionBootstrap(this.className, expression, this.params, this.inheritEnv);
    }


    public void addParam(final FunctionParam name) {
        this.params.add(name);
    }


    @Override
    public void onShiftRight(final Token<?> headToken) {
        this.codeGenerator.onShiftRight(headToken);
    }


    @Override
    public void onShiftLeft(final Token<?> headToken) {
        this.codeGenerator.onShiftLeft(headToken);
    }


    @Override
    public void onUnsignedShiftRight(final Token<?> headToken) {
        this.codeGenerator.onUnsignedShiftRight(headToken);
    }


    @Override
    public void onAssignment(final Token<?> headToken) {
        this.codeGenerator.onAssignment(headToken);
    }


    @Override
    public void onBitOr(final Token<?> headToken) {
        this.codeGenerator.onBitOr(headToken);
    }


    @Override
    public void onBitAnd(final Token<?> headToken) {
        this.codeGenerator.onBitAnd(headToken);
    }


    @Override
    public void onBitXor(final Token<?> headToken) {
        this.codeGenerator.onBitXor(headToken);
    }


    @Override
    public void onBitNot(final Token<?> headToken) {
        this.codeGenerator.onBitNot(headToken);
    }


    @Override
    public void onAdd(final Token<?> headToken) {
        this.codeGenerator.onAdd(headToken);
    }

    @Override
    public void onSub(final Token<?> headToken) {
        this.codeGenerator.onSub(headToken);
    }


    @Override
    public void onMult(final Token<?> headToken) {
        this.codeGenerator.onMult(headToken);
    }


    @Override
    public void onExponent(final Token<?> token) {
        this.codeGenerator.onExponent(token);
    }


    @Override
    public void onDiv(final Token<?> headToken) {
        this.codeGenerator.onDiv(headToken);
    }


    @Override
    public void onAndLeft(final Token<?> headToken) {
        this.codeGenerator.onAndLeft(headToken);
    }


    @Override
    public void onAndRight(final Token<?> headToken) {
        this.codeGenerator.onAndRight(headToken);
    }


    @Override
    public void onTernaryBoolean(final Token<?> headToken) {
        this.codeGenerator.onTernaryBoolean(headToken);
    }


    @Override
    public void onTernaryLeft(final Token<?> headToken) {
        this.codeGenerator.onTernaryLeft(headToken);
    }


    @Override
    public void onTernaryRight(final Token<?> headToken) {
        this.codeGenerator.onTernaryRight(headToken);
    }


    @Override
    public void onTernaryEnd(final Token<?> headToken) {
        this.codeGenerator.onTernaryEnd(headToken);
    }


    @Override
    public void onJoinLeft(final Token<?> headToken) {
        this.codeGenerator.onJoinLeft(headToken);
    }


    @Override
    public void onJoinRight(final Token<?> headToken) {
        this.codeGenerator.onJoinRight(headToken);
    }


    @Override
    public void onEq(final Token<?> headToken) {
        this.codeGenerator.onEq(headToken);
    }


    @Override
    public void onMatch(final Token<?> headToken) {
        this.codeGenerator.onMatch(headToken);
    }


    @Override
    public void onNeq(final Token<?> headToken) {
        this.codeGenerator.onNeq(headToken);
    }


    @Override
    public void onLt(final Token<?> headToken) {
        this.codeGenerator.onLt(headToken);
    }


    @Override
    public void onLe(final Token<?> headToken) {
        this.codeGenerator.onLe(headToken);
    }


    @Override
    public void onGt(final Token<?> headToken) {
        this.codeGenerator.onGt(headToken);
    }


    @Override
    public void onGe(final Token<?> headToken) {
        this.codeGenerator.onGe(headToken);
    }


    @Override
    public void onMod(final Token<?> headToken) {
        this.codeGenerator.onMod(headToken);
    }


    @Override
    public void onNot(final Token<?> headToken) {
        this.codeGenerator.onNot(headToken);
    }


    @Override
    public void onNeg(final Token<?> headToken) {
        this.codeGenerator.onNeg(headToken);
    }


    @Override
    public Script getResult(final boolean unboxObject) {
        return this.codeGenerator.getResult(unboxObject);
    }

    @Override
    public void genNewLambdaCode(LambdaFunctionBootstrap bootstrap) {

    }


    @Override
    public void onConstant(final Token<?> headToken) {
        this.codeGenerator.onConstant(headToken);
    }

    @Override
    public void onMethodName(final Token<?> headToken) {
        this.codeGenerator.onMethodName(headToken);
    }


    @Override
    public void onMethodParameter(final Token<?> headToken) {
        this.codeGenerator.onMethodParameter(headToken);
    }

    @Override
    public void onMethodInvoke(final Token<?> headToken) {
        this.codeGenerator.onMethodInvoke(headToken);
    }


    @Override
    public void onLambdaDefineStart(final Token<?> headToken) {
        this.codeGenerator.onLambdaDefineStart(headToken);
    }


    @Override
    public void onLambdaArgument(final Token<?> headToken, final FunctionParam param) {
        this.codeGenerator.onLambdaArgument(headToken, param);
    }

    @Override
    public void onLambdaBodyStart(final Token<?> headToken) {
        this.codeGenerator.onLambdaBodyStart(headToken);
    }

    @Override
    public void onLambdaBodyEnd(final Token<?> headToken) {
        this.parentCodeGenerator.onLambdaBodyEnd(headToken);
    }

    @Override
    public void onArray(final Token<?> headToken) {
        this.codeGenerator.onArray(headToken);
    }

    @Override
    public void onArrayIndexStart(final Token<?> token) {
        this.codeGenerator.onArrayIndexStart(token);
    }

    @Override
    public void onArrayIndexEnd(final Token<?> headToken) {
        this.codeGenerator.onArrayIndexEnd(headToken);
    }

}
