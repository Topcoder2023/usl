package com.gitee.usl.grammar.runtime.function.internal;

import java.util.ArrayList;
import java.util.List;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._RuntimeJavaType;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Constants;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemFunction
public class UnpackingArgsFunction extends BasicFunction {

    private final _Function fn;

    public UnpackingArgsFunction(final _Function fn) {
        this.fn = fn;
    }

    @Override
    public String name() {
        return this.fn.name();
    }

    @Override
    public _Object execute(Env env, _Object... arguments) {
        List<_Object> realArgList = new ArrayList<>(arguments.length + 10);

        for (_Object arg : arguments) {
            if (arg.meta(Constants.UNPACK_ARGS) != null) {
                for (Object obj : RuntimeUtils.seq(arg.getValue(env), env)) {
                    realArgList.add(_RuntimeJavaType.valueOf(obj));
                }
            } else {
                realArgList.add(arg);
            }
        }

        _Object[] realArgs = new _Object[realArgList.size()];
        realArgs = realArgList.toArray(realArgs);

        return fn.execute(env, realArgs);
    }

}
