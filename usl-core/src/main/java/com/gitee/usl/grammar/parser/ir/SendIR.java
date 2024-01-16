package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.BasicCall;
import com.gitee.usl.grammar.runtime.function.TraceFunction;
import com.gitee.usl.grammar.runtime.type.AviatorFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

/**
 * @author hongda.li
 */
public record SendIR(String name, int arity, boolean unpackArgs, int funcId) implements IR, BasicCall {

    @Override
    public void eval(final InterpretContext context) {
        AviatorFunction fn;
        if (this.name != null) {
            fn = RuntimeUtils.getFunction(context.getEnv(), this.name);
        } else {
            fn = null;
        }

        int i = this.arity;
        AviatorObject[] args = new AviatorObject[this.arity];

        while (--i >= 0) {
            args[i] = context.pop();
        }

        if (this.name == null) {
            fn = (AviatorFunction) context.pop();
        }

        if (RuntimeUtils.isTracedEval(context.getEnv())) {
            fn = TraceFunction.wrapTrace(fn);
        }

        if (this.unpackArgs) {
            fn = RuntimeUtils.unpackArgsFunction(fn);
        }

        if (this.funcId >= 0) {
            // put function arguments ref id to env.
            context.getEnv().put("__fas__", this.funcId);
        }
        context.push(this.basicCall(fn, args, this.arity, context.getEnv()));
        context.dispatch();
    }

}
