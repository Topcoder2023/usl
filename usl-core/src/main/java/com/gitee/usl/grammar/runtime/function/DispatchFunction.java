package com.gitee.usl.grammar.runtime.function;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.type.AviatorRuntimeJavaType;
import com.gitee.usl.grammar.utils.Env;
import com.googlecode.aviator.exception.FunctionNotFoundException;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Dispatch function by argument arity.
 *
 * @author dennis(killme2008 @ gmail.com)
 */
@SystemFunction
public class DispatchFunction extends AbstractVariadicFunction implements BasicCall {

    private static final Object[] EMPTY_VAR_ARGS = new Object[0];

    private static final long serialVersionUID = 5993768652338524385L;

    private final IdentityHashMap<Integer, LambdaFunction> functions = new IdentityHashMap<>();

    private final TreeMap<Integer, LambdaFunction> variadicFunctions = new TreeMap<>();

    private final String name;

    public DispatchFunction(final String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void install(final LambdaFunction fn) {
        if (fn.isVariadic()) {
            this.variadicFunctions.put(fn.getArity(), fn);
        } else {
            this.functions.put(fn.getArity(), fn);
        }
        fn.setInstalled(true);
    }

    @Override
    public AviatorObject variadicCall(final Map<String, Object> env, AviatorObject... args) {
        final int arity = args.length;
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

        if (fn.isVariadic()) {
            args = processVariadicArgs(env, arity, fn, args);
        }

        return basicCall(fn, args, arity, (Env) env);
    }

    static AviatorObject[] processVariadicArgs(final Map<String, Object> env, final int arity,
                                               final LambdaFunction fn, AviatorObject[] args) {
        if (arity + 1 == fn.getArity()) {
            AviatorObject[] newArgs = new AviatorObject[arity + 1];
            System.arraycopy(args, 0, newArgs, 0, arity);
            newArgs[arity] = AviatorRuntimeJavaType.valueOf(EMPTY_VAR_ARGS);

            args = newArgs;
        } else {
            AviatorObject[] newArgs = new AviatorObject[fn.getArity()];
            System.arraycopy(args, 0, newArgs, 0, fn.getArity() - 1);
            Object[] varArgs = new Object[arity - fn.getArity() + 1];

            for (int i = 0; i < varArgs.length; i++) {
                varArgs[i] = args[fn.getArity() - 1 + i].getValue(env);
            }

            newArgs[fn.getArity() - 1] = AviatorRuntimeJavaType.valueOf(varArgs);

            args = newArgs;
        }
        return args;
    }

}
