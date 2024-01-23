package com.gitee.usl.grammar.runtime.type;

import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.utils.Reflector;
import com.gitee.usl.grammar.utils.TypeUtils;
import com.gitee.usl.grammar.utils.Utils;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.grammar.Options;
import com.gitee.usl.grammar.exception.CompareNotSupportedException;
import com.gitee.usl.grammar.exception.ExpressionRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author hongda.li
 */
@Setter
@Getter
@Accessors(chain = true)
public abstract class _Object {

    private Token<?> from;

    protected StringMap<Object> metadata = new StringMap<>();

    public Object meta(final Object key) {
        return this.metadata.get(String.valueOf(key));
    }

    public int compare(final _Object other, final Map<String, Object> env) {
        return compare(other, env, false);
    }

    public int compareEq(final _Object other, final Map<String, Object> env) {
        return compare(other, env, true);
    }

    private int compare(final _Object other, final Map<String, Object> env,
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

    public int innerCompare(_Object other, Map<String, Object> env) {
        throw new UnsupportedOperationException();
    }


    public abstract _Type getAviatorType();


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


    public _Object match(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support match operation '=~'");
    }


    public _Object neg(final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support negative operation '-'");
    }

    public _Object setValue(final _Object value, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Can't assign value " + value.desc(env) + " to " + desc(env));
    }

    public _Object defineValue(final _Object value, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Can't assign value " + value.desc(env) + " to " + desc(env));
    }

    public _Object not(final Map<String, Object> env) {
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


    public _Object add(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not add " + desc(env) + " with " + other.desc(env));
    }


    public _Object bitAnd(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitAnd " + desc(env) + " with " + other.desc(env));
    }


    public _Object bitOr(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitOr " + desc(env) + " with " + other.desc(env));
    }


    public _Object bitXor(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not bitXor " + desc(env) + " with " + other.desc(env));
    }


    public _Object shiftRight(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not shiftRight " + desc(env) + " with " + other.desc(env));
    }


    public _Object shiftLeft(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not shiftLeft " + desc(env) + " with " + other.desc(env));
    }


    public _Object unsignedShiftRight(final _Object other,
                                      final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not unsignedShiftRight " + desc(env) + " with " + other.desc(env));
    }


    public _Object bitNot(final Map<String, Object> env) {
        throw new ExpressionRuntimeException(desc(env) + " doesn't support not operation '^'");
    }


    public _Object sub(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not sub " + desc(env) + " with " + other.desc(env));
    }


    public _Object mod(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not mod " + desc(env) + " with " + other.desc(env));
    }


    public _Object div(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException("Could not div " + desc(env) + " with " + other.desc(env));
    }


    public _Object mult(final _Object other, final Map<String, Object> env) {
        throw new ExpressionRuntimeException(
                "Could not mult " + desc(env) + " with " + other.desc(env));
    }

    public _Object exponent(final _Object other, final Map<String, Object> env) {
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

    public _Object self(final Map<String, Object> env) {
        return this;
    }

    public Object self(final Map<String, Object> env, boolean unbox) {
        return unbox ? this.getValue(env) : self(env);
    }

    /**
     * Access array or list element
     *
     * @param env
     * @param indexObject
     * @return
     */
    public _Object getElement(final Map<String, Object> env, final _Object indexObject) {
        throw new ExpressionRuntimeException(desc(env) + " is not a array");
    }
}
