package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemComponent;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemComponent
public class ConstantFunction extends BasicFunction {
    private final String name;
    private final _Object result;

    public ConstantFunction(final String name, final _Object result) {
        this.name = name;
        this.result = result;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public _Object execute(Env env, _Object[] arguments) {
        return this.result;
    }

}
