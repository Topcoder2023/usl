package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;

public class AssertTypeIR implements IR {

    public enum AssertTypes {
        Number, String, Bool,
    }

    private final AssertTypes type;

    public AssertTypeIR(final AssertTypes type) {
        super();
        this.type = type;
    }

    @Override
    public void eval(final InterpretContext context) {
        switch (this.type) {
            case Bool:
                context.peek().booleanValue(context.getEnv());
                break;
            case Number:
                context.peek().numberValue(context.getEnv());
                break;
            case String:
                context.peek().stringValue(context.getEnv());
                break;
        }
        context.dispatch();
    }

    @Override
    public String toString() {
        return "assert " + this.type.name().toLowerCase();
    }

}
