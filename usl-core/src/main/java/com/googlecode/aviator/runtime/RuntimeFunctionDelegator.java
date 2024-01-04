package com.googlecode.aviator.runtime;

import java.util.Map;

import cn.hutool.core.text.StrPool;
import com.gitee.usl.api.annotation.Description;
import com.googlecode.aviator.FunctionMissing;
import com.googlecode.aviator.exception.FunctionNotFoundException;
import com.gitee.usl.grammar.ScriptKeyword;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.system.ConstantFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorObject;
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

    private AviatorFunction getFunc(final Map<String, Object> env, final AviatorObject... args) {
        if (this.containsDot && this.subNames == null) {
            this.subNames = Constants.SPLIT_PAT.split(this.name);
        }
        AviatorFunction func = this.tryGetFuncFromEnv(env);
        if (func != null) {
            return func;
        }
        if (this.functionMissing != null) {
            return new ConstantFunction(this.name, this.functionMissing.onFunctionMissing(this.name, env, args));
        }
        throw new FunctionNotFoundException("Function not found: " + this.name);
    }

    private AviatorFunction tryGetFuncFromEnv(final Map<String, Object> env) {
        Object val = AviatorJavaType.getValueFromEnv(this.name, this.containsDot, this.subNames, env, false, true);
        if (val instanceof AviatorFunction) {
            return (AviatorFunction) val;
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
    public AviatorObject call(final Map<String, Object> env) {
        return getFunc(env).call(env);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1) {
        return getFunc(env, arg1)
                .call(env, arg1);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2) {
        return getFunc(env, arg1, arg2)
                .call(env, arg1, arg2);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3) {
        return getFunc(env, arg1, arg2, arg3)
                .call(env, arg1, arg2, arg3);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4) {
        return getFunc(env, arg1, arg2, arg3, arg4)
                .call(env, arg1, arg2, arg3, arg4);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5)
                .call(env, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
                arg13).call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
                arg13);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13,
                              final AviatorObject arg14) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13,
                              final AviatorObject arg14,
                              final AviatorObject arg15) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13,
                              final AviatorObject arg14,
                              final AviatorObject arg15,
                              final AviatorObject arg16) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13,
                              final AviatorObject arg14,
                              final AviatorObject arg15,
                              final AviatorObject arg16,
                              final AviatorObject arg17) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13,
                              final AviatorObject arg14,
                              final AviatorObject arg15,
                              final AviatorObject arg16,
                              final AviatorObject arg17,
                              final AviatorObject arg18) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13,
                              final AviatorObject arg14,
                              final AviatorObject arg15,
                              final AviatorObject arg16,
                              final AviatorObject arg17,
                              final AviatorObject arg18,
                              final AviatorObject arg19) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
    }

    @Override
    public AviatorObject call(final Map<String, Object> env,
                              final AviatorObject arg1,
                              final AviatorObject arg2,
                              final AviatorObject arg3,
                              final AviatorObject arg4,
                              final AviatorObject arg5,
                              final AviatorObject arg6,
                              final AviatorObject arg7,
                              final AviatorObject arg8,
                              final AviatorObject arg9,
                              final AviatorObject arg10,
                              final AviatorObject arg11,
                              final AviatorObject arg12,
                              final AviatorObject arg13,
                              final AviatorObject arg14,
                              final AviatorObject arg15,
                              final AviatorObject arg16,
                              final AviatorObject arg17,
                              final AviatorObject arg18,
                              final AviatorObject arg19,
                              final AviatorObject arg20) {
        return getFunc(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20)
                .call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
    }

}
