package com.gitee.usl.grammar.runtime.function;

import com.gitee.usl.grammar.runtime.type._Type;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;

import java.util.Map;

/**
 * @author hongda.li
 */
public abstract class BasicFunction extends _Object implements _Function {

    @Override
    public _Type getAviatorType() {
        return _Type.Lambda;
    }

    @Override
    public String desc(final Map<String, Object> env) {
        return "[Function - " + name() + "]";
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return this;
    }

}
