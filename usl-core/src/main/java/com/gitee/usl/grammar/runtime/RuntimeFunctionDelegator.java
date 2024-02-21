package com.gitee.usl.grammar.runtime;

import java.util.Map;

import cn.hutool.core.text.StrPool;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.SystemComponent;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.function.system.ConstantFunction;
import com.gitee.usl.grammar.runtime.type._JavaType;
import com.gitee.usl.api.FunctionMissing;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.exception.FunctionNotFoundException;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Constants;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Description("委托函数")
@SystemComponent
public final class RuntimeFunctionDelegator extends BasicFunction {

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

    private _Function getFunc(final Map<String, Object> env, final _Object... args) {
        if (this.containsDot && this.subNames == null) {
            this.subNames = Constants.SPLIT_PAT.split(this.name);
        }
        _Function func = this.tryGetFuncFromEnv(env);
        if (func != null) {
            return func;
        }
        if (this.functionMissing != null) {
            return new ConstantFunction(this.name, this.functionMissing.onFunctionMissing(this.name, env, args));
        }
        throw new FunctionNotFoundException("Function not found: " + this.name);
    }

    private _Function tryGetFuncFromEnv(final Map<String, Object> env) {
        Object val = _JavaType.getValueFromEnv(this.name, this.containsDot, this.subNames, env, false, true);
        if (val instanceof _Function) {
            return (_Function) val;
        }
        return null;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public _Object execute(Env env, _Object... arguments) {
        return getFunc(env, arguments).execute(env, arguments);
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return this.tryGetFuncFromEnv(env);
    }

}
