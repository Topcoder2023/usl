package com.googlecode.aviator.runtime.type;

import com.gitee.usl.infra.structure.StringMap;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.utils.Env;
import com.googlecode.aviator.utils.Reflector;
import com.googlecode.aviator.utils.TypeUtils;
import com.googlecode.aviator.utils.Utils;
import lombok.Getter;

import java.util.Map;

/**
 * @author hongda.li
 */
@Getter
public abstract class AviatorObject {

    protected StringMap<Object> metadata = new StringMap<>();

    public AviatorObject withMeta(final Object key, final Object value) {
        this.metadata.put(String.valueOf(key), value);
        return this;
    }

    public Object meta(final Object key) {
        return this.metadata.get(String.valueOf(key));
    }

    public AviatorObject withoutMeta(final Object key) {
        this.metadata.remove(String.valueOf(key));
        return this;
    }

    public int compare(final AviatorObject other, final Map<String, Object> env) {
        return compare(other, env, false);
    }

    public int compareEq(final AviatorObject other, final Map<String, Object> env) {
        return compare(other, env, true);
    }

    private int compare(final AviatorObject other, final Map<String, Object> env,
                        final boolean isEq) {
        if (this == other) {
            return 0;
        }
        try {
            return innerCompare(other, env);
        } catch (CompareNotSupportedException t) {
            if (isEq) {
                if (RuntimeUtils.getInstance(env).getOptionValue(Options.TRACE_EVAL).bool) {
                    t.printStackTrace();
                }
                return 1;
            }
            throw Reflector.sneakyThrow(t);
        } catch (Throwable t) {
            throw Reflector.sneakyThrow(t);
        }
    }

    public abstract int innerCompare(AviatorObject other, Map<String, Object> env);


    public abstract AviatorType getAviatorType();


    @Override
    public String toString() {
        return desc(Env.EMPTY_ENV);
    }


    /**
     * Returns true if the aviator object is null.
     *
     * @return
     * @since 3.0.0
     */
    public boolean isNull(final Map<String, Object> env) {
        return getValue(env) == null;
    }


    public AviatorObject match(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support match operation '=~'");
    }


    public AviatorObject neg(final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support negative operation '-'");
    }

    public AviatorObject setValue(final AviatorObject value, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Can't assign value " + value.desc(env) + " to " + desc(env));
    }

    public AviatorObject defineValue(final AviatorObject value, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Can't assign value " + value.desc(env) + " to " + desc(env));
    }

    public AviatorObject not(final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support not operation '!'");
    }

    public String desc(final Map<String, Object> env) {
        Object val = getValue(env);
        if (val != this) {
            return "<" + getAviatorType() + ", " + val + ">";
        } else {
            return "<" + getAviatorType() + ", this>";
        }
    }

    public abstract Object getValue(Map<String, Object> env);


    public AviatorObject add(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not add " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject bitAnd(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitAnd " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject bitOr(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitOr " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject bitXor(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitXor " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject shiftRight(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not shiftRight " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject shiftLeft(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not shiftLeft " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject unsignedShiftRight(final AviatorObject other,
                                            final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not unsignedShiftRight " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject bitNot(final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support not operation '^'");
    }


    public AviatorObject sub(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not sub " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject mod(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not mod " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject div(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not div " + desc(env) + " with " + other.desc(env));
    }


    public AviatorObject mult(final AviatorObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not mult " + desc(env) + " with " + other.desc(env));
    }

    public AviatorObject exponent(final AviatorObject other, final Map<String, Object> env) {
        Object base = getValue(env);
        Object exp = other.getValue(env);

        if (!(base instanceof Number) || !(exp instanceof Number)) {
            throw new ExpressionRuntimeException(
                    "Could not exponent " + desc(env) + " with " + other.desc(env));
        }
        return Utils.exponent((Number) base, (Number) exp, env);
    }


    public Number numberValue(final Map<String, Object> env) {
        if (!(getValue(env) instanceof Number)) {
            throw new ExpressionRuntimeException(desc(env) + " is not a number value");
        }
        return (Number) getValue(env);
    }


    public String stringValue(final Map<String, Object> env) {
        Object value = getValue(env);
        if (!(TypeUtils.isString(value))) {
            throw new ExpressionRuntimeException(desc(env) + " is not a string value");
        }
        return String.valueOf(value);
    }


    public boolean booleanValue(final Map<String, Object> env) {
        final Object val = getValue(env);
        if (!(val instanceof Boolean)) {
            throw new ExpressionRuntimeException(desc(env) + " is not a boolean value");
        }
        return (boolean) val;
    }

    public AviatorObject deref(final Map<String, Object> env) {
        return this;
    }

    /**
     * Access array or list element
     *
     * @param env
     * @param indexObject
     * @return
     */
    public AviatorObject getElement(final Map<String, Object> env, final AviatorObject indexObject) {
        throw new ExpressionRuntimeException(desc(env) + " is not a array");
    }
}
