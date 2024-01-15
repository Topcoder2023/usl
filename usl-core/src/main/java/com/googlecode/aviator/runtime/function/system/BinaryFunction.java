package com.googlecode.aviator.runtime.function.system;

import java.util.Map;

import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.op.OperationRuntime;
import com.gitee.usl.grammar.type.USLObject;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Getter
public class BinaryFunction extends AbstractFunction {

    private final OperatorType opType;

    public BinaryFunction(final OperatorType opType) {
        super();
        this.opType = opType;
    }

    @Override
    public String getName() {
        return this.opType.getToken();
    }

    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2) {
        return OperationRuntime.eval(arg1, arg2, env, this.opType);
    }

    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1) {
        return switch (this.opType) {
            case BIT_AND, BIT_OR, BIT_XOR, ADD, SUB, MULT, Exponent, DIV, MOD -> throwArity(1);
            case NOT, NEG -> OperationRuntime.eval(arg1, null, env, this.opType);
            default -> throw new ExpressionRuntimeException("Invalid binary operator");
        };
    }

}
