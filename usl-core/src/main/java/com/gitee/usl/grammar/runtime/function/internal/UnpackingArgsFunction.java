package com.gitee.usl.grammar.runtime.function.internal;

import java.util.ArrayList;
import java.util.List;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type.AviatorRuntimeJavaType;
import com.gitee.usl.grammar.runtime.type.Function;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Constants;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemFunction
public class UnpackingArgsFunction extends BasicFunction {

    private final Function fn;

    public UnpackingArgsFunction(final Function fn) {
        this.fn = fn;
    }

    @Override
    public String name() {
        return this.fn.name();
    }

    @Override
    public AviatorObject execute(Env env, AviatorObject... arguments) {
        List<AviatorObject> realArgList = new ArrayList<>(arguments.length + 10);

        for (AviatorObject arg : arguments) {
            if (arg.meta(Constants.UNPACK_ARGS) != null) {
                for (Object obj : RuntimeUtils.seq(arg.getValue(env), env)) {
                    realArgList.add(AviatorRuntimeJavaType.valueOf(obj));
                }
            } else {
                realArgList.add(arg);
            }
        }

        AviatorObject[] realArgs = new AviatorObject[realArgList.size()];
        realArgs = realArgList.toArray(realArgs);

        return fn.execute(env, realArgs);
    }

}
