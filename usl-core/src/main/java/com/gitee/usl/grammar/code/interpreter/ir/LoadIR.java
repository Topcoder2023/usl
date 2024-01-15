package com.gitee.usl.grammar.code.interpreter.ir;


import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;
import com.gitee.usl.grammar.runtime.type.*;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.gitee.usl.grammar.lexer.token.NumberToken;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.lexer.token.Variable;
import com.gitee.usl.grammar.parser.VariableMeta;
import com.gitee.usl.grammar.utils.Constants;
import com.gitee.usl.grammar.utils.TypeUtils;

/**
 * load an operand
 *
 * @author dennis(killme2008 @ gmail.com)
 */
public class LoadIR implements IR {
    private static final long serialVersionUID = -688605619463290104L;
    private final Token<?> token;
    private final VariableMeta meta;
    private final String sourceFile;
    private final boolean inConstantPool = false;

    public LoadIR(final String sourceFile, final Token<?> token, final VariableMeta meta,
                  final boolean inConstantPool) {
        super();
        this.token = token;
        this.sourceFile = sourceFile;
        this.meta = meta;
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
            context.push(constant);
            return;
        }

        // load token to stack
        switch (this.token.getType()) {
            case Number:
                // load numbers
                NumberToken numberToken = (NumberToken) this.token;
                Number number = numberToken.getNumber();

                if (TypeUtils.isBigInt(number)) {
                    context.push(AviatorBigInt.valueOf(this.token.getLexeme()));
                } else if (TypeUtils.isDecimal(number)) {
                    context.push(AviatorDecimal.valueOf(context.getEnv(), this.token.getLexeme()));
                } else if (TypeUtils.isDouble(number)) {
                    context.push(AviatorDouble.valueOf(number.doubleValue()));
                } else {
                    context.push(AviatorLong.valueOf(number.longValue()));
                }
                break;
            case String:
                context.push(new AviatorString((String) this.token.getValue(null), true,
                        this.token.getMeta(Constants.INTER_META, true), this.token.getLineNo()));
                break;
            case Pattern:
                context.push(new AviatorPattern((String) this.token.getValue(null)));
                break;
            case Variable:
                if (this.token == Variable.TRUE) {
                    context.push(AviatorBoolean.TRUE);
                } else if (this.token == Variable.FALSE) {
                    context.push(AviatorBoolean.FALSE);
                } else if (this.token == Variable.NIL) {
                    context.push(AviatorNil.NIL);
                } else {
                    AviatorJavaType var;
                    if (this.meta != null) {
                        var = context.loadVar(this.meta);
                        assert (var != null);
                    } else {
                        var = new AviatorJavaType(this.token.getLexeme());
                    }
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
