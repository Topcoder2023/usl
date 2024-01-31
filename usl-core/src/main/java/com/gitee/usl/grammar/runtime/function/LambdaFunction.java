package com.gitee.usl.grammar.runtime.function;

import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.type._RuntimeJavaType;
import com.gitee.usl.grammar.script.BS;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.runtime.FunctionParam;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.type._JavaType;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.runtime.type._Type;
import com.gitee.usl.grammar.utils.Env;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hongda.li
 */
@SystemFunction
public final class LambdaFunction extends BasicFunction {

    private final List<FunctionParam> params;

    @Getter
    private final BS expression;

    @Setter
    @Getter
    private Env context;

    @Setter
    @Getter
    private boolean inheritEnv = false;

    private final String name;

    @Getter
    @Setter
    private boolean installed;

    public void resetContext() {
        if (this.inheritEnv) {
            this.context = null;
        }
    }

    public LambdaFunction(final String name,
                          final List<FunctionParam> params,
                          final Script expression,
                          final Env context) {
        this.name = name;
        this.params = params;
        this.context = context;
        this.expression = (BS) expression;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public _Object execute(Env env, _Object... arguments) {
        try {
            return _RuntimeJavaType.valueOf(this.expression.defaultImpl(newEnv(env, arguments)));
        } finally {
            if (this.inheritEnv) {
                this.context = null;
            }
        }
    }

    public int getArity() {
        return this.params.size();
    }

    private Map<String, Object> newEnv(final Map<String, Object> parentEnv,
                                       final _Object... args) {
        Env env = new Env(parentEnv);
        if (CollUtil.isNotEmpty(this.context)) {
            env.putAll(this.context);
        }
        env.configure(this.context.getInstance(), this.expression);

        if (args.length != this.params.size()) {
            throw new IllegalArgumentException("Wrong number of args(" + args.length + ") passed to "
                    + name() + "(" + this.params.size() + ")");
        }

        for (int i = 0; i < this.params.size(); i++) {
            FunctionParam param = this.params.get(i);
            final _Object arg = args[i];
            Object value = arg.getValue(parentEnv);
            if (value == null && arg.getAviatorType() == _Type.JavaType
                    && !parentEnv.containsKey(((_JavaType) arg).getName())) {
                value = RuntimeUtils.getInstance(parentEnv).getFunction(((_JavaType) arg).getName());
            }
            env.override(param.getName(), value);
        }

        return env;
    }

}
