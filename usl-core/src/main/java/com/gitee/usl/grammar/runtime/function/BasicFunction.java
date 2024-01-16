package com.gitee.usl.grammar.runtime.function;

import com.gitee.usl.grammar.runtime.type.AviatorType;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.runtime.type.Function;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author hongda.li
 */
public abstract class BasicFunction extends AviatorObject implements Function {

    @Override
    public AviatorType getAviatorType() {
        return AviatorType.Lambda;
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
