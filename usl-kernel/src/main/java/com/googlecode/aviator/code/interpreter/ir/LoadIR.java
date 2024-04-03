package com.googlecode.aviator.code.interpreter.ir;

import com.googlecode.aviator.code.interpreter.IR;
import com.googlecode.aviator.code.interpreter.InterpretContext;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.lexer.token.NumberToken;
import com.googlecode.aviator.lexer.token.Token;
import com.googlecode.aviator.lexer.token.Variable;
import com.googlecode.aviator.parser.VariableMeta;
import com.googlecode.aviator.runtime.type.*;
import com.googlecode.aviator.utils.Constants;
import com.googlecode.aviator.utils.TypeUtils;

import java.io.Serial;
import java.util.Objects;

/**
 * @author hongda.li
 */
public class LoadIR implements IR {
    public static final String PROPERTY_FROM_TOKEN = "FromToken";
    @Serial
    private static final long serialVersionUID = -688605619463290104L;
    private final Token<?> token;
    private final VariableMeta meta;
    private final String sourceFile;
    private final boolean inConstantPool;

    public LoadIR(final String sourceFile,
                  final Token<?> token,
                  final VariableMeta meta,
                  final boolean inConstantPool) {
        this.meta = meta;
        this.token = token;
        this.sourceFile = sourceFile;
        this.inConstantPool = inConstantPool;
    }

    /**
     * 针对 AviatorObject 设置来源 Token
     *
     * @param object AviatorObject
     * @param token  来源 Token
     */
    public static void setFromToken(AviatorObject object, Token<?> token) {
        Objects.requireNonNull(object);
        object.withMeta(PROPERTY_FROM_TOKEN, token);
    }

    /**
     * 获取 AviatorObject 中的来源 Token
     *
     * @param object AviatorObject
     * @return 来源 Token
     */
    public static Token<?> getFromToken(AviatorObject object) {
        Objects.requireNonNull(object);
        return (Token<?>) object.meta(PROPERTY_FROM_TOKEN);
    }

    @Override
    public void eval(final InterpretContext context) {
        evalWithoutDispatch(context);
        context.dispatch();
    }

    public void evalWithoutDispatch(final InterpretContext context) {
        if (this.token == null) {
            return;
        }

        if (this.inConstantPool) {
            final AviatorObject constant = context.loadConstant(this.token);
            assert (constant != null);
            setFromToken(constant, this.token);
            context.push(constant);
            return;
        }

        switch (this.token.getType()) {
            case Number:
                NumberToken numberToken = (NumberToken) this.token;
                Number number = numberToken.getNumber();

                if (TypeUtils.isBigInt(number)) {
                    AviatorBigInt aviatorBigInt = AviatorBigInt.valueOf(this.token.getLexeme());
                    setFromToken(aviatorBigInt, this.token);
                    context.push(aviatorBigInt);
                } else if (TypeUtils.isDecimal(number)) {
                    AviatorDecimal aviatorDecimal = AviatorDecimal.valueOf(context.getEnv(), this.token.getLexeme());
                    setFromToken(aviatorDecimal, this.token);
                    context.push(aviatorDecimal);
                } else if (TypeUtils.isDouble(number)) {
                    AviatorDouble aviatorDouble = AviatorDouble.valueOf(number.doubleValue());
                    setFromToken(aviatorDouble, this.token);
                    context.push(aviatorDouble);
                } else {
                    AviatorLong aviatorLong = AviatorLong.valueOf(number.longValue());
                    setFromToken(aviatorLong, this.token);
                    context.push(aviatorLong);
                }
                break;
            case String:
                AviatorString aviatorString = new AviatorString((String) this.token.getValue(null), true,
                        this.token.getMeta(Constants.INTER_META, true), this.token.getLineNo());
                setFromToken(aviatorString, this.token);
                context.push(aviatorString);
                break;
            case Pattern:
                AviatorPattern aviatorPattern = new AviatorPattern((String) this.token.getValue(null));
                setFromToken(aviatorPattern, this.token);
                context.push(aviatorPattern);
                break;
            case Variable:
                if (this.token == Variable.TRUE) {
                    AviatorBoolean aviatorBoolean = new AviatorBoolean(Boolean.TRUE);
                    setFromToken(aviatorBoolean, this.token);
                    context.push(aviatorBoolean);
                } else if (this.token == Variable.FALSE) {
                    AviatorBoolean aviatorBoolean = new AviatorBoolean(Boolean.FALSE);
                    setFromToken(aviatorBoolean, this.token);
                    context.push(aviatorBoolean);
                } else if (this.token == Variable.NIL) {
                    AviatorNil aviatorNil = new AviatorNil();
                    setFromToken(aviatorNil, this.token);
                    context.push(aviatorNil);
                } else {
                    AviatorJavaType var;
                    if (this.meta != null) {
                        var = context.loadVar(this.meta);
                        assert (var != null);
                    } else {
                        var = new AviatorJavaType(this.token.getLexeme());
                    }
                    setFromToken(var, this.token);
                    context.push(var);
                }
                break;
            default:
                throw new ExpressionRuntimeException("Can't load " + this.token);
        }
    }

    @Override
    public String toString() {
        return "load " + this.token.getLexeme() + "  [" + this.token.getType() + "]      ("
                + this.sourceFile + ":" + this.token.getLineNo() + ")";
    }
}
