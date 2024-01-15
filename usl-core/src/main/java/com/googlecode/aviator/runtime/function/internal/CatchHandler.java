package com.googlecode.aviator.runtime.function.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gitee.usl.grammar.type.USLFunction;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorType;
import com.googlecode.aviator.utils.Env;
import com.googlecode.aviator.utils.Reflector;

/**
 * @author dennis(killme2008 @ gmail.com)
 */
public class CatchHandler extends USLObject {
    /**
     *
     */
    private static final long serialVersionUID = 2718902412145274738L;
    private final USLFunction func;
    private final List<Class<?>> exceptionClasses;

    public CatchHandler(final Env env, final USLFunction func,
                        final List<String> exceptionClassNames) {
        super();
        this.func = func;
        this.exceptionClasses = new ArrayList<>(exceptionClassNames.size());
        for (String exceptionClass : exceptionClassNames) {
            try {
                this.exceptionClasses.add(env.resolveClassSymbol(exceptionClass, false));
            } catch (Exception e) {
                throw Reflector.sneakyThrow(e);
            }
        }
    }

    public USLFunction getFunc() {
        return this.func;
    }

    public boolean isMatch(final Class<?> eClass) {
        for (Class<?> clazz : this.exceptionClasses) {
            if (clazz.isAssignableFrom(eClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int innerCompare(final USLObject other, final Map<String, Object> env) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AviatorType getAviatorType() {
        return AviatorType.JavaType;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return this;
    }

}
