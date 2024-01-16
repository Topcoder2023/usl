package com.gitee.usl.grammar.runtime.function;

import java.util.List;
import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.type.AviatorRuntimeJavaType;
import com.gitee.usl.grammar.script.BS;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.runtime.FunctionParam;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.runtime.type.AviatorType;
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
    public AviatorObject execute(Env env, AviatorObject... arguments) {
        try {
            return AviatorRuntimeJavaType.valueOf(this.expression.defaultImpl(newEnv(env, arguments)));
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
                                       final AviatorObject... args) {
        Env env;
        if (!this.inheritEnv) {
            final Env contextEnv = new Env(parentEnv, this.context);
            contextEnv.configure(this.context.getInstance(), this.expression);
            env = new Env(contextEnv);
            env.configure(this.context.getInstance(), this.expression);
        } else {
            env = (Env) parentEnv;
        }

        if (args.length != this.params.size()) {
            throw new IllegalArgumentException("Wrong number of args(" + args.length + ") passed to "
                    + name() + "(" + this.params.size() + ")");
        }

        for (int i = 0; i < this.params.size(); i++) {
            FunctionParam param = this.params.get(i);
            final AviatorObject arg = args[i];
            Object value = arg.getValue(parentEnv);
            if (value == null && arg.getAviatorType() == AviatorType.JavaType
                    && !parentEnv.containsKey(((AviatorJavaType) arg).getName())) {
                value = RuntimeUtils.getInstance(parentEnv).getFunction(((AviatorJavaType) arg).getName());
            }
            env.override(param.getName(), value);
        }

        return env;
    }

}
