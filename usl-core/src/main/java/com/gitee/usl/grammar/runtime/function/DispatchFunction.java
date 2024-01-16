package com.gitee.usl.grammar.runtime.function;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.utils.Env;
import com.googlecode.aviator.exception.FunctionNotFoundException;
import com.gitee.usl.grammar.runtime.type._Object;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hongda.li
 */
@SystemFunction
public class DispatchFunction extends BasicFunction {

    private final IdentityHashMap<Integer, LambdaFunction> functions = new IdentityHashMap<>();

    private final TreeMap<Integer, LambdaFunction> variadicFunctions = new TreeMap<>();

    private final String name;

    public DispatchFunction(final String name) {
        super();
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    public void install(final LambdaFunction fn) {
        this.functions.put(fn.getArity(), fn);
        fn.setInstalled(true);
    }

    @Override
    public _Object execute(final Env env, _Object[] arguments) {
        final int arity = arguments.length;
        LambdaFunction fn = this.functions.get(arity);

        if (fn == null) {
            // 1. try to retrieve the variadic by arity+1
            fn = this.variadicFunctions.get(arity + 1);

            if (fn == null) {
                Map.Entry<Integer, LambdaFunction> entry = this.variadicFunctions.floorEntry(arity);
                if (entry != null) {
                    fn = entry.getValue();
                }
            }

            if (fn == null) {
                throw new FunctionNotFoundException(
                        "Function `" + this.name + "` with args(" + arity + ") not found");
            }
        }
        assert (arity + 1 >= arity);

        return fn.execute(env, arguments);
    }

}
