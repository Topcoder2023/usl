package com.gitee.usl.grammar.runtime.op;

import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.utils.Env;
import com.googlecode.aviator.exception.IllegalArityException;
import com.gitee.usl.grammar.lexer.token.OperatorType;
import com.gitee.usl.grammar.lexer.token.Variable;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;

import java.util.Map;

/**
 * Operation runtime
 *
 * @author dennis
 */
public class OperationRuntime {

    private static final ThreadLocal<_Object[]> TWO_ARRGS = new ThreadLocal<_Object[]>() {

        @Override
        protected _Object[] initialValue() {
            return new _Object[2];
        }

    };

    private static final ThreadLocal<_Object[]> ONE_ARG = new ThreadLocal<_Object[]>() {

        @Override
        protected _Object[] initialValue() {
            return new _Object[1];
        }

    };

    /**
     * Eval with arguments array.
     *
     * @param args
     * @param opType
     * @return
     */
    public static _Object eval(final Map<String, Object> env, final _Object[] args,
                               final OperatorType opType) {
        _Function func = RuntimeUtils.getInstance(env).getOpFunction(opType);
        _Object ret = eval0(env, args, opType, func);
        if (RuntimeUtils.isTracedEval(env)) {
            trace(env, opType, ret, args);
        }
        return ret;
    }

    private static _Object eval0(final Map<String, Object> env, final _Object[] args,
                                 final OperatorType opType, final _Function func) {
        if (func == null) {
            return opType.eval(args, env);
        } else {
            return evalOpFunction(env, args, opType, func);
        }
    }

    public static _Object evalOpFunction(final Map<String, Object> env,
                                         final _Object[] args, final OperatorType opType, final _Function func) {
        return switch (opType.getArity()) {
            case 1 -> func.execute((Env) env, args[0]);
            case 2 -> func.execute((Env) env, args[0], args[1]);
            case 3 -> func.execute((Env) env, args[0], args[1], args[2]);
            default -> throw new IllegalArityException("Too many arguments.");
        };
    }

    /**
     * Eval with unary operator
     *
     * @param arg
     * @param env
     * @param opType
     * @return
     */
    public static _Object eval(final _Object arg, final Map<String, Object> env,
                               final OperatorType opType) {
        _Function func = RuntimeUtils.getInstance(env).getOpFunction(opType);
        _Object ret = eval0(arg, env, opType, func);
        if (RuntimeUtils.isTracedEval(env)) {
            trace(env, opType, ret, arg);
        }
        return ret;
    }

    private static _Object eval0(final _Object arg, final Map<String, Object> env,
                                 final OperatorType opType, final _Function func) {
        if (func == null) {
            _Object[] args = ONE_ARG.get();
            args[0] = arg;
            return opType.eval(args, env);
        } else {
            return func.execute((Env) env, arg);
        }
    }

    /**
     * Just like {@link #eval(_Object, _Object, Map, OperatorType)}, but with difference
     * arguments order.
     *
     * @param left
     * @param env
     * @param right
     * @param opType
     * @return
     */
    public static _Object eval(final _Object left, final Map<String, Object> env,
                               final _Object right, final OperatorType opType) {
        return eval(left, right, env, opType);
    }

    /**
     * Eval with binary operator
     *
     * @param left
     * @param right
     * @param env
     * @param opType
     * @return
     */
    public static _Object eval(final _Object left, final _Object right,
                               final Map<String, Object> env, final OperatorType opType) {

        _Function func = RuntimeUtils.getInstance(env).getOpFunction(opType);
        _Object ret = eval0(left, right, env, opType, func);
        if (RuntimeUtils.isTracedEval(env)) {
            trace(env, opType, ret, left, right);
        }
        return ret;
    }

    private static _Object eval0(final _Object left, final _Object right,
                                 final Map<String, Object> env, final OperatorType opType, final _Function func) {
        if (func == null) {
            _Object[] args = TWO_ARRGS.get();
            args[0] = left;
            args[1] = right;
            return opType.eval(args, env);
        } else {
            return func.execute((Env) env, left, right);
        }
    }

    public static boolean containsOpFunction(final Map<String, Object> env,
                                             final OperatorType opType) {
        return RuntimeUtils.getInstance(env).getOperatorFunctionMap().containsKey(opType);
    }

    private static final String WHITE_SPACE = " ";
    private static final String TRACE_PREFIX = "         ";

    private static String desc(final _Object arg, final Map<String, Object> env) {
        if (arg != null) {
            return arg.desc(env);
        } else {
            return Variable.NIL.getLexeme();
        }
    }

    private static void trace(final Map<String, Object> env, final OperatorType opType,
                              final _Object result, final _Object... args) {

        StringBuilder argsDec = new StringBuilder();
        argsDec.append(desc(args[0], env));
        for (int i = 1; i < args.length; i++) {
            if (args[i] != null) {
                argsDec.append(WHITE_SPACE).append(opType.token).append(WHITE_SPACE)
                        .append(desc(args[i], env));
            }
        }

        RuntimeUtils.printlnTrace(env, TRACE_PREFIX + argsDec + " => " + desc(result, env));
    }
}
