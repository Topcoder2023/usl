package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;
import lombok.ToString;

@ToString
public class NewLambdaIR implements IR {

    private final String lambdaName;

    public NewLambdaIR(final String lambdaName) {
        this.lambdaName = lambdaName;
    }

    @Override
    public void eval(final InterpretContext context) {
        context.push(context.getExpression().newLambda(context.getEnv(), this.lambdaName));
        context.dispatch();
    }

}
