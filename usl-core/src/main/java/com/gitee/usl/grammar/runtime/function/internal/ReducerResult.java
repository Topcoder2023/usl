package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.type._RuntimeJavaType;
import com.gitee.usl.grammar.runtime.type._Type;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.runtime.type._Object;

import java.util.Map;

/**
 * @author hongda.li
 */
@SystemFunction
public class ReducerResult extends _RuntimeJavaType {
    public final ReducerState state;
    public _Object obj;

    public boolean isEmptyState() {
        return this.state == ReducerState.Empty;
    }

    public static ReducerResult withEmpty(final _Object obj) {
        return new ReducerResult(ReducerState.Empty, obj);
    }

    public static ReducerResult withCont(final _Object obj) {
        return new ReducerResult(ReducerState.Cont, obj);
    }

    public static ReducerResult withBreak(final _Object obj) {
        return new ReducerResult(ReducerState.Break, obj);
    }

    public static ReducerResult withReturn(final _Object obj) {
        return new ReducerResult(ReducerState.Return, obj);
    }

    private ReducerResult(final ReducerState state, final _Object obj) {
        super(obj);
        this.state = state;
        this.obj = obj;
        this.metadata = obj.getMetadata();
    }

    @Override
    public _Object self(final Map<String, Object> env) {
        this.obj = this.obj.self(env);
        return this;
    }

    @Override
    public int innerCompare(final _Object other, final Map<String, Object> env) {
        return this.obj.innerCompare(other, env);
    }

    @Override
    public _Type getAviatorType() {
        return this.obj.getAviatorType();
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        if (this.obj == this) {
            return this;
        }
        return this.obj.getValue(env);
    }

    @Override
    public String toString() {
        Object val = getValue(Env.EMPTY_ENV);
        if (val != this) {
            return "<Reducer, " + this.state.name() + ", " + val + ">";
        } else {
            return "<Reducer, " + this.state.name() + ", this>";
        }
    }

    @Override
    public boolean isNull(final Map<String, Object> env) {
        return this.obj.isNull(env);
    }

    @Override
    public int hashCode() {
        return this.obj.hashCode();
    }

    @Override
    public _Object match(final _Object other, final Map<String, Object> env) {
        return this.obj.match(other, env);
    }

    @Override
    public _Object neg(final Map<String, Object> env) {
        return this.obj.neg(env);
    }

    @Override
    public _Object setValue(final _Object value, final Map<String, Object> env) {
        return this.obj.setValue(value, env);
    }

    @Override
    public _Object not(final Map<String, Object> env) {
        return this.obj.not(env);
    }

    @Override
    public String desc(final Map<String, Object> env) {
        return this.obj.desc(env);
    }

    @Override
    public _Object add(final _Object other, final Map<String, Object> env) {
        return this.obj.add(other, env);
    }

    @Override
    public _Object bitAnd(final _Object other, final Map<String, Object> env) {
        return this.obj.bitAnd(other, env);
    }

    @Override
    public _Object bitOr(final _Object other, final Map<String, Object> env) {
        return this.obj.bitOr(other, env);
    }

    @Override
    public _Object bitXor(final _Object other, final Map<String, Object> env) {
        return this.obj.bitXor(other, env);
    }

    @Override
    public _Object shiftRight(final _Object other, final Map<String, Object> env) {
        return this.obj.shiftRight(other, env);
    }

    @Override
    public boolean equals(final Object obj) {
        return this.obj.equals(obj);
    }

    @Override
    public _Object shiftLeft(final _Object other, final Map<String, Object> env) {
        return this.obj.shiftLeft(other, env);
    }

    @Override
    public _Object unsignedShiftRight(final _Object other,
                                      final Map<String, Object> env) {
        return this.obj.unsignedShiftRight(other, env);
    }

    @Override
    public _Object bitNot(final Map<String, Object> env) {
        return this.obj.bitNot(env);
    }

    @Override
    public _Object sub(final _Object other, final Map<String, Object> env) {
        return this.obj.sub(other, env);
    }

    @Override
    public _Object mod(final _Object other, final Map<String, Object> env) {
        return this.obj.mod(other, env);
    }

    @Override
    public _Object div(final _Object other, final Map<String, Object> env) {
        return this.obj.div(other, env);
    }

    @Override
    public _Object mult(final _Object other, final Map<String, Object> env) {
        return this.obj.mult(other, env);
    }

    @Override
    public Number numberValue(final Map<String, Object> env) {
        return this.obj.numberValue(env);
    }

    @Override
    public String stringValue(final Map<String, Object> env) {
        return this.obj.stringValue(env);
    }

    @Override
    public boolean booleanValue(final Map<String, Object> env) {
        return this.obj.booleanValue(env);
    }

    @Override
    public _Object getElement(final Map<String, Object> env, final _Object indexObject) {
        return this.obj.getElement(env, indexObject);
    }

}
