package com.gitee.usl.grammar.runtime.function;

import com.gitee.usl.grammar.runtime.type.AviatorFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.runtime.type.AviatorType;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.googlecode.aviator.exception.CompareNotSupportedException;

import java.util.Map;

/**
 * @author hongda.li
 */
public abstract class AbstractFunction extends AviatorObject implements AviatorFunction {

    @Override
    public AviatorObject call() throws Exception {
        return this.call(Env.EMPTY_ENV);
    }

    @Override
    public void run() {
        this.call(Env.EMPTY_ENV);
    }

    public AviatorObject throwArity(final int n) {
        throw new USLExecuteException(ResultCode.NOT_MATCH_OF_ARGUMENT_COUNT);
    }

    @Override
    public String desc(final Map<String, Object> env) {
        return "<" + getAviatorType() + ", " + getName() + ">";
    }

    @Override
    public AviatorObject call(final Map<String, Object> env) {
        return throwArity(0);
    }


    @Override
    public int innerCompare(final AviatorObject other, final Map<String, Object> env) {
        throw new CompareNotSupportedException("Lambda function can't be compared.");
    }


    @Override
    public AviatorType getAviatorType() {
        return AviatorType.Lambda;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return this;
    }

    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {
        return throwArity(1);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2) {
        return throwArity(2);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3) {
        return throwArity(3);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4) {
        return throwArity(4);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5) {
        return throwArity(5);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6) {
        return throwArity(6);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7) {
        return throwArity(7);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8) {
        return throwArity(8);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9) {
        return throwArity(9);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10) {
        return throwArity(10);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11) {
        return throwArity(11);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12) {
        return throwArity(12);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13) {
        return throwArity(13);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14) {
        return throwArity(14);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14, final AviatorObject arg15) {
        return throwArity(15);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14, final AviatorObject arg15, final AviatorObject arg16) {
        return throwArity(16);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14, final AviatorObject arg15, final AviatorObject arg16,
                              final AviatorObject arg17) {
        return throwArity(17);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14, final AviatorObject arg15, final AviatorObject arg16,
                              final AviatorObject arg17, final AviatorObject arg18) {
        return throwArity(18);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14, final AviatorObject arg15, final AviatorObject arg16,
                              final AviatorObject arg17, final AviatorObject arg18, final AviatorObject arg19) {
        return throwArity(19);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14, final AviatorObject arg15, final AviatorObject arg16,
                              final AviatorObject arg17, final AviatorObject arg18, final AviatorObject arg19,
                              final AviatorObject arg20) {
        return throwArity(20);
    }


    @Override
    public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
                              final AviatorObject arg2, final AviatorObject arg3, final AviatorObject arg4,
                              final AviatorObject arg5, final AviatorObject arg6, final AviatorObject arg7,
                              final AviatorObject arg8, final AviatorObject arg9, final AviatorObject arg10,
                              final AviatorObject arg11, final AviatorObject arg12, final AviatorObject arg13,
                              final AviatorObject arg14, final AviatorObject arg15, final AviatorObject arg16,
                              final AviatorObject arg17, final AviatorObject arg18, final AviatorObject arg19,
                              final AviatorObject arg20, final AviatorObject... args) {
        return throwArity(21);
    }

}
