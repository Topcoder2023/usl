package com.gitee.usl.grammar.runtime.type;

import java.util.Map;

import com.gitee.usl.grammar.utils.TypeUtils;

/**
 * @author hongda.li
 */
public class _Null extends _Object {

    public static final _Null NIL = new _Null();

    @Override
    public _Object add(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case String:
                return new _String("null" + other.getValue(env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (TypeUtils.isString(otherValue)) {
                    return new _String("null" + otherValue);
                } else {
                    return super.add(other, env);
                }
            default:
                return super.add(other, env);
        }
    }


    @Override
    public int innerCompare(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case Nil:
                return 0;
            case JavaType:
                if (other.getValue(env) == null) {
                    return 0;
                }
        }
        return -1;
    }

    @Override
    public _Type getAviatorType() {
        return _Type.Nil;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return null;
    }

}
