package com.manda.usl.kernel.view;

import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public final class FunctionSession {

    private final Env env;

    private final AviatorObject[] objects;

    private final FunctionMeta definition;

    private Invocation<?> invocation;

    private Object result;

    private Exception exception;

    public FunctionSession(Env env, AviatorObject[] objects, FunctionMeta definition) {
        this.env = env;
        this.objects = objects;
        this.definition = definition;
    }
}
