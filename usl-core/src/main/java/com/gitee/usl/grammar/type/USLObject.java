package com.gitee.usl.grammar.type;

import com.gitee.usl.infra.structure.StringMap;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.type.AviatorType;
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
public abstract class USLObject {

    protected StringMap<Object> metadata = new StringMap<>();

    public USLObject withMeta(final Object key, final Object value) {
        this.metadata.put(String.valueOf(key), value);
        return this;
    }

    public Object meta(final Object key) {
        return this.metadata.get(String.valueOf(key));
    }

    public USLObject withoutMeta(final Object key) {
        this.metadata.remove(String.valueOf(key));
        return this;
    }

    public int compare(final USLObject other, final Map<String, Object> env) {
        return compare(other, env, false);
    }

    public int compareEq(final USLObject other, final Map<String, Object> env) {
        return compare(other, env, true);
    }

    private int compare(final USLObject other, final Map<String, Object> env,
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

    public abstract int innerCompare(USLObject other, Map<String, Object> env);


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


    public USLObject match(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support match operation '=~'");
    }


    public USLObject neg(final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support negative operation '-'");
    }

    public USLObject setValue(final USLObject value, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Can't assign value " + value.desc(env) + " to " + desc(env));
    }

    public USLObject defineValue(final USLObject value, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Can't assign value " + value.desc(env) + " to " + desc(env));
    }

    public USLObject not(final Map<String, Object> env) {
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


    public USLObject add(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not add " + desc(env) + " with " + other.desc(env));
    }


    public USLObject bitAnd(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitAnd " + desc(env) + " with " + other.desc(env));
    }


    public USLObject bitOr(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitOr " + desc(env) + " with " + other.desc(env));
    }


    public USLObject bitXor(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitXor " + desc(env) + " with " + other.desc(env));
    }


    public USLObject shiftRight(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not shiftRight " + desc(env) + " with " + other.desc(env));
    }


    public USLObject shiftLeft(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not shiftLeft " + desc(env) + " with " + other.desc(env));
    }


    public USLObject unsignedShiftRight(final USLObject other,
                                        final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not unsignedShiftRight " + desc(env) + " with " + other.desc(env));
    }


    public USLObject bitNot(final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support not operation '^'");
    }


    public USLObject sub(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not sub " + desc(env) + " with " + other.desc(env));
    }


    public USLObject mod(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not mod " + desc(env) + " with " + other.desc(env));
    }


    public USLObject div(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not div " + desc(env) + " with " + other.desc(env));
    }


    public USLObject mult(final USLObject other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not mult " + desc(env) + " with " + other.desc(env));
    }

    public USLObject exponent(final USLObject other, final Map<String, Object> env) {
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

    public USLObject deref(final Map<String, Object> env) {
        return this;
    }

    /**
     * Access array or list element
     *
     * @param env
     * @param indexObject
     * @return
     */
    public USLObject getElement(final Map<String, Object> env, final USLObject indexObject) {
        throw new ExpressionRuntimeException(desc(env) + " is not a array");
    }
}
