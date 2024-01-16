package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemFunction
public class ConstantFunction extends BasicFunction {
    private final String name;
    private final AviatorObject result;

    public ConstantFunction(final String name, final AviatorObject result) {
        this.name = name;
        this.result = result;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public AviatorObject execute(Env env, AviatorObject[] arguments) {
        return this.result;
    }

}
