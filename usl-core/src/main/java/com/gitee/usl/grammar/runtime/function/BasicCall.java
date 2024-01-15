package com.gitee.usl.grammar.runtime.function;

import com.gitee.usl.grammar.runtime.type.AviatorFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
public interface BasicCall {

    default AviatorObject basicCall(final AviatorFunction fn,
                                    final AviatorObject[] args,
                                    final int arity,
                                    final Env env) {

        if (arity == 0) {
            return fn.call(env);
        }

        switch (arity) {
            case 1:
                return fn.call(env,
                        args[0]);
            case 2:
                return fn.call(env,
                        args[0],
                        args[1]);
            case 3:
                return fn.call(env,
                        args[0],
                        args[1],
                        args[2]);
            case 4:
                return fn.call(env,
                        args[0],
                        args[1],
                        args[2],
                        args[3]);
            case 5:
                return fn.call(env,
                        args[0],
                        args[1],
                        args[2],
                        args[3],
                        args[4]);
            case 6:
                return fn.call(env,
                        args[0],
                        args[1],
                        args[2],
                        args[3],
                        args[4],
                        args[5]);
            case 7:
                return fn.call(env,
                        args[0],
                        args[1],
                        args[2],
                        args[3],
                        args[4],
                        args[5],
                        args[6]);
            case 8:
                return fn.call(env,
                        args[0],
                        args[1],
                        args[2],
                        args[3],
                        args[4],
                        args[5],
                        args[6],
                        args[7]);
            case 9:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8]);
            case 10:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9]);
            case 11:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10]);
            case 12:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11]);
            case 13:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12]);
            case 14:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13]);
            case 15:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13], args[14]);
            case 16:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15]);
            case 17:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16]);
            case 18:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                        args[17]);
            case 19:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                        args[17], args[18]);
            case 20:
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                        args[17], args[18], args[19]);
            default:
                AviatorObject[] remainingArgs = new AviatorObject[args.length - 20];
                System.arraycopy(args, 20, remainingArgs, 0, remainingArgs.length);
                return fn.call(env, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
                        args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                        args[17], args[18], args[19], remainingArgs);
        }
    }
}
