package com.googlecode.aviator.runtime;

import java.util.Map;

import cn.hutool.core.text.StrPool;
import com.gitee.usl.api.annotation.Description;
import com.googlecode.aviator.FunctionMissing;
import com.googlecode.aviator.exception.FunctionNotFoundException;
import com.gitee.usl.grammar.ScriptKeyword;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.system.ConstantFunction;
import com.gitee.usl.grammar.type.USLFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.utils.Constants;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Description("委托函数")
public final class RuntimeFunctionDelegator extends AbstractFunction {

    @Description("函数名称")
    private final String name;

    @Getter
    @Description("函数名称截取部分")
    private String[] subNames;

    @Getter
    @Description("是否包含<.>分隔符")
    private final boolean containsDot;

    @Getter
    @Description("函数兜底机制")
    private final FunctionMissing functionMissing;

    public RuntimeFunctionDelegator(final String name,
                                    final ScriptKeyword keyword,
                                    final FunctionMissing functionMissing) {
        if (keyword != null) {
            this.name = keyword.reserve(name).getLexeme();
        } else {
            this.name = name;
        }
        this.functionMissing = functionMissing;
        this.containsDot = this.name.contains(StrPool.DOT);
    }

    private USLFunction getFunc(final Map<String, Object> env, final USLObject... args) {
        if (this.containsDot && this.subNames == null) {
            this.subNames = Constants.SPLIT_PAT.split(this.name);
        }
        USLFunction func = this.tryGetFuncFromEnv(env);
        if (func != null) {
            return func;
        }
        if (this.functionMissing != null) {
            return new ConstantFunction(this.name, this.functionMissing.onFunctionMissing(this.name, env, args));
        }
        throw new FunctionNotFoundException("Function not found: " + this.name);
    }

    private USLFunction tryGetFuncFromEnv(final Map<String, Object> env) {
        Object val = AviatorJavaType.getValueFromEnv(this.name, this.containsDot, this.subNames, env, false, true);
        if (val instanceof USLFunction) {
            return (USLFunction) val;
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return this.tryGetFuncFromEnv(env);
    }

    @Override
    public USLObject call(final Map<String, Object> env) {
        return getFunc(env).call(env);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1) {
        return getFunc(env, arg1)
                .call(env, arg1);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2) {
        return getFunc(env, arg1, arg2)
                .call(env, arg1, arg2);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3) {
        return getFunc(env, arg1, arg2, arg3)
                .call(env, arg1, arg2, arg3);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4) {
        return getFunc(env, arg1, arg2, arg3, arg4)
                .call(env, arg1, arg2, arg3, arg4);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5)
                .call(env, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
                arg13).call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
                arg13);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13,
                          final USLObject arg14) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13,
                          final USLObject arg14,
                          final USLObject arg15) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13,
                          final USLObject arg14,
                          final USLObject arg15,
                          final USLObject arg16) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13,
                          final USLObject arg14,
                          final USLObject arg15,
                          final USLObject arg16,
                          final USLObject arg17) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13,
                          final USLObject arg14,
                          final USLObject arg15,
                          final USLObject arg16,
                          final USLObject arg17,
                          final USLObject arg18) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13,
                          final USLObject arg14,
                          final USLObject arg15,
                          final USLObject arg16,
                          final USLObject arg17,
                          final USLObject arg18,
                          final USLObject arg19) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
    }

    @Override
    public USLObject call(final Map<String, Object> env,
                          final USLObject arg1,
                          final USLObject arg2,
                          final USLObject arg3,
                          final USLObject arg4,
                          final USLObject arg5,
                          final USLObject arg6,
                          final USLObject arg7,
                          final USLObject arg8,
                          final USLObject arg9,
                          final USLObject arg10,
                          final USLObject arg11,
                          final USLObject arg12,
                          final USLObject arg13,
                          final USLObject arg14,
                          final USLObject arg15,
                          final USLObject arg16,
                          final USLObject arg17,
                          final USLObject arg18,
                          final USLObject arg19,
                          final USLObject arg20) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
    }

}
