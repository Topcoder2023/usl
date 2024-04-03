package com.googlecode.aviator.runtime.type;

import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.utils.TypeUtils;

import java.io.Serial;
import java.util.Map;

/**
 * @author hongda.li
 */
public class AviatorBoolean extends AviatorObject {
    @Serial
    private static final long serialVersionUID = -685795150869373183L;

    Boolean value;

    public static final AviatorBoolean TRUE = new AviatorBoolean(Boolean.TRUE);

    public static final AviatorBoolean FALSE = new AviatorBoolean(Boolean.FALSE);

    public AviatorBoolean(final Boolean value) {
        this.value = value;
    }
    @Override
    public AviatorObject not(final Map<String, Object> env) {
        return this.value ? FALSE : TRUE;
    }

    @Override
    public final boolean booleanValue(final Map<String, Object> env) {
        return this.value;
    }

    public boolean getBooleanValue() {
        return this.value;
    }

    @Override
    public AviatorObject add(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case String:
                return new AviatorString(this.value.toString() + ((AviatorString) other).getLexeme(env));
            case JavaType:
                AviatorJavaType javaType = (AviatorJavaType) other;
                final Object otherJavaValue = javaType.getValue(env);
                if (TypeUtils.isString(otherJavaValue)) {
                    return new AviatorString(this.value.toString() + otherJavaValue);
                } else {
                    return super.add(other, env);
                }
            default:
                return super.add(other, env);
        }
    }

    @Override
    public AviatorType getAviatorType() {
        return AviatorType.Boolean;
    }

    @Override
    public final Object getValue(final Map<String, Object> env) {
        return this.value;
    }

    public static AviatorBoolean valueOf(final boolean b) {
        return b ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
    }

    @Override
    public int innerCompare(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case Boolean:
                AviatorBoolean otherBoolean = (AviatorBoolean) other;
                return this.value.compareTo(otherBoolean.value);
            case JavaType:
                AviatorJavaType javaType = (AviatorJavaType) other;
                final Object otherValue = javaType.getValue(env);
                if (otherValue == null) {
                    return 1;
                }
                if (otherValue instanceof Boolean) {
                    return this.value.compareTo((Boolean) otherValue);
                } else {
                    throw new CompareNotSupportedException(
                            "Could not compare " + desc(env) + " with " + other.desc(env));
                }
            case Nil:
                return 1;
            default:
                throw new CompareNotSupportedException(
                        "Could not compare " + desc(env) + " with " + other.desc(env));
        }
    }
}
