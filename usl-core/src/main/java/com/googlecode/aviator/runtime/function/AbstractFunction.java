package com.googlecode.aviator.runtime.function;

import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.gitee.usl.grammar.type.USLFunction;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorType;
import com.googlecode.aviator.utils.Env;

import java.util.Map;

/**
 * @author hongda.li
 */
public abstract class AbstractFunction extends USLObject implements USLFunction {

    @Override
    public USLObject call() throws Exception {
        return this.call(Env.EMPTY_ENV);
    }

    @Override
    public void run() {
        this.call(Env.EMPTY_ENV);
    }

    public USLObject throwArity(final int n) {
        throw new USLExecuteException(ResultCode.NOT_MATCH_OF_ARGUMENT_COUNT);
    }

    @Override
    public String desc(final Map<String, Object> env) {
        return "<" + getAviatorType() + ", " + getName() + ">";
    }

    @Override
    public USLObject call(final Map<String, Object> env) {
        return throwArity(0);
    }


    @Override
    public int innerCompare(final USLObject other, final Map<String, Object> env) {
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
    public USLObject call(final Map<String, Object> env, final USLObject arg1) {
        return throwArity(1);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2) {
        return throwArity(2);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3) {
        return throwArity(3);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4) {
        return throwArity(4);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5) {
        return throwArity(5);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6) {
        return throwArity(6);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7) {
        return throwArity(7);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8) {
        return throwArity(8);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9) {
        return throwArity(9);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10) {
        return throwArity(10);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11) {
        return throwArity(11);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12) {
        return throwArity(12);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13) {
        return throwArity(13);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14) {
        return throwArity(14);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14, final USLObject arg15) {
        return throwArity(15);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14, final USLObject arg15, final USLObject arg16) {
        return throwArity(16);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14, final USLObject arg15, final USLObject arg16,
                          final USLObject arg17) {
        return throwArity(17);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14, final USLObject arg15, final USLObject arg16,
                          final USLObject arg17, final USLObject arg18) {
        return throwArity(18);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14, final USLObject arg15, final USLObject arg16,
                          final USLObject arg17, final USLObject arg18, final USLObject arg19) {
        return throwArity(19);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14, final USLObject arg15, final USLObject arg16,
                          final USLObject arg17, final USLObject arg18, final USLObject arg19,
                          final USLObject arg20) {
        return throwArity(20);
    }


    @Override
    public USLObject call(final Map<String, Object> env, final USLObject arg1,
                          final USLObject arg2, final USLObject arg3, final USLObject arg4,
                          final USLObject arg5, final USLObject arg6, final USLObject arg7,
                          final USLObject arg8, final USLObject arg9, final USLObject arg10,
                          final USLObject arg11, final USLObject arg12, final USLObject arg13,
                          final USLObject arg14, final USLObject arg15, final USLObject arg16,
                          final USLObject arg17, final USLObject arg18, final USLObject arg19,
                          final USLObject arg20, final USLObject... args) {
        return throwArity(21);
    }

}
