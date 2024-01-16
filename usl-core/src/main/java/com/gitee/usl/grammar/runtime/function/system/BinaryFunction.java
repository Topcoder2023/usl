package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.lexer.token.OperatorType;
import com.gitee.usl.grammar.runtime.op.OperationRuntime;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Getter
@SystemFunction
public class BinaryFunction extends BasicFunction {

    private final OperatorType opType;

    public BinaryFunction(final OperatorType opType) {
        this.opType = opType;
    }

    @Override
    public String name() {
        return this.opType.getToken();
    }

    @Override
    public AviatorObject execute(Env env, AviatorObject[] arguments) {
        AviatorObject left = arguments[0];
        AviatorObject right = arguments.length == 1 ? null : arguments[1];
        return OperationRuntime.eval(left, right, env, this.opType);
    }

}
