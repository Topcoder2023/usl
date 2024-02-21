package com.gitee.usl.grammar.parser;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.runtime.FunctionParam;
import com.gitee.usl.grammar.runtime.LambdaFunctionBootstrap;

/**
 * @author hongda.li
 */
@Description("字节码生成器接口")
public interface Generator {

    Script getResult(boolean unboxObject);

    void genNewLambdaCode(final LambdaFunctionBootstrap bootstrap);

    void onAssignment(Token<?> token);

    void setParser(Parser parser);

    void onShiftRight(Token<?> token);

    void onShiftLeft(Token<?> token);


    void onUnsignedShiftRight(Token<?> token);


    void onBitOr(Token<?> token);


    void onBitAnd(Token<?> token);


    void onBitXor(Token<?> token);


    void onBitNot(Token<?> token);


    void onAdd(Token<?> token);


    void onSub(Token<?> token);


    @SuppressWarnings("SpellCheckingInspection")
    void onMult(Token<?> token);

    void onExponent(Token<?> token);


    void onDiv(Token<?> token);


    void onAndLeft(Token<?> token);


    void onAndRight(Token<?> token);

    @Description("三元表达式的布尔表达式部分")
    void onTernaryBoolean(Token<?> token);

    void onTernaryLeft(Token<?> token);


    void onTernaryRight(Token<?> token);

    void onTernaryEnd(Token<?> token);


    void onJoinLeft(Token<?> token);


    void onJoinRight(Token<?> token);


    void onEq(Token<?> token);


    void onMatch(Token<?> token);


    void onNeq(Token<?> token);


    void onLt(Token<?> token);


    void onLe(Token<?> token);


    void onGt(Token<?> token);


    void onGe(Token<?> token);


    void onMod(Token<?> token);


    void onNot(Token<?> token);


    void onNeg(Token<?> token);

    void onConstant(Token<?> token);

    void onMethodName(Token<?> token);

    void onMethodParameter(Token<?> token);

    void onMethodInvoke(Token<?> token);

    void onLambdaDefineStart(Token<?> token);

    void onLambdaArgument(Token<?> token, FunctionParam param);

    void onLambdaBodyStart(Token<?> token);

    void onLambdaBodyEnd(Token<?> token);

    void onArray(Token<?> token);

    void onArrayIndexStart(Token<?> token);

    void onArrayIndexEnd(Token<?> token);

}
