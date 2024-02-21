package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.infra.structure.wrapper.ObjectWrapper;

/**
 * @author hongda.li
 */
public record SendIR(String name, int arity, boolean unpackArgs, int funcId) implements IR {

    @Override
    public void eval(final InterpretContext context) {
        @Description("参数的数量")
        IntWrapper wrapper = new IntWrapper(this.arity);

        ObjectWrapper<_Function> fn = new ObjectWrapper<>();

        if (this.name != null) {
            fn.set(RuntimeUtils.getFunction(context.getEnv(), this.name));
        }

        _Object[] args = new _Object[this.arity];

        while (wrapper.decrementAndGet() >= 0) {
            args[wrapper.get()] = context.pop();
        }

        if (this.name == null) {
            fn.set((_Function) context.pop());
        }

        if (this.unpackArgs) {
            fn.set(RuntimeUtils.unpackArgsFunction(fn.get()));
        }

        context.push(fn.get().execute(context.getEnv(), args));

        context.dispatch();
    }

}
