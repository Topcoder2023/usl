package com.gitee.usl.grammar.code.interpreter.ir;

import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;
import com.gitee.usl.grammar.runtime.type.*;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.gitee.usl.grammar.lexer.token.NumberToken;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.lexer.token.Variable;
import com.gitee.usl.grammar.utils.Constants;
import com.gitee.usl.grammar.utils.TypeUtils;

/**
 * @author hongda.li
 */
public record LoadIR(Token<?> token) implements IR {

    @Override
    public void eval(final InterpretContext context) {
        if (this.token == null) {
            context.dispatch();
            return;
        }

        switch (this.token.getType()) {
            case Number:
                Number number = ((NumberToken) this.token).getNumber();

                if (TypeUtils.isBigInt(number)) {
                    context.push(AviatorBigInt.valueOf(this.token.getLexeme()), this.token);
                } else if (TypeUtils.isDecimal(number)) {
                    context.push(AviatorDecimal.valueOf(context.getEnv(), this.token.getLexeme()), this.token);
                } else if (TypeUtils.isDouble(number)) {
                    context.push(AviatorDouble.valueOf(number.doubleValue()), this.token);
                } else {
                    context.push(AviatorLong.valueOf(number.longValue()), this.token);
                }
                break;
            case String:
                context.push(new AviatorString((String) this.token.getValue(null),
                                true,
                                this.token.getMeta(Constants.INTER_META, true),
                                this.token.getLineNo()),
                        this.token);
                break;
            case Pattern:
                context.push(new AviatorPattern((String) this.token.getValue(null)), this.token);
                break;
            case Variable:
                if (this.token == Variable.TRUE) {
                    context.push(AviatorBoolean.TRUE, this.token);
                } else if (this.token == Variable.FALSE) {
                    context.push(AviatorBoolean.FALSE, this.token);
                } else if (this.token == Variable.NIL) {
                    context.push(AviatorNil.NIL, this.token);
                } else {
                    AviatorJavaType var = new AviatorJavaType(this.token.getLexeme());
                    context.push(var, this.token);
                }
                break;
            default:
                throw new USLCompileException(ResultCode.COMPILE_FAILURE);
        }

        context.dispatch();
    }

}
