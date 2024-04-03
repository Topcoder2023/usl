package com.googlecode.aviator.runtime.type;

import com.googlecode.aviator.utils.TypeUtils;

import java.io.Serial;
import java.util.Map;

/**
 * @author hongda.li
 */
public class AviatorNil extends AviatorObject {
    @Serial
    private static final long serialVersionUID = 5030890238879926682L;
    public static final AviatorNil NIL = new AviatorNil();

    @Override
    public AviatorObject add(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case String:
                return new AviatorString("null" + other.getValue(env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (TypeUtils.isString(otherValue)) {
                    return new AviatorString("null" + otherValue);
                } else {
                    return super.add(other, env);
                }
            default:
                return super.add(other, env);
        }
    }

    @Override
    public int innerCompare(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case Nil:
                return 0;
            case JavaType:
                if (other.getValue(env) == null) {
                    return 0;
                }
        }
        // Any object is greater than nil except nil
        return -1;
    }


    @Override
    public AviatorType getAviatorType() {
        return AviatorType.Nil;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return null;
    }

}
