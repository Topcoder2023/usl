package com.googlecode.aviator.runtime.function.internal;

import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.AviatorType;
import com.googlecode.aviator.utils.Env;

import java.util.Map;

/**
 * ReducerResult in looping.
 *
 * @author dennis(killme2008@gmail.com)
 * @since 5.0.0
 */
public class ReducerResult extends AviatorRuntimeJavaType {

  private static final long serialVersionUID = 8804868778622599851L;
  public final ReducerState state;
  public USLObject obj;

  public boolean isEmptyState() {
    return this.state == ReducerState.Empty;
  }

  public static ReducerResult withEmpty(final USLObject obj) {
    return new ReducerResult(ReducerState.Empty, obj);
  }

  public static ReducerResult withCont(final USLObject obj) {
    return new ReducerResult(ReducerState.Cont, obj);
  }

  public static ReducerResult withBreak(final USLObject obj) {
    return new ReducerResult(ReducerState.Break, obj);
  }

  public static ReducerResult withReturn(final USLObject obj) {
    return new ReducerResult(ReducerState.Return, obj);
  }

  private ReducerResult(final ReducerState state, final USLObject obj) {
    super(obj);
    this.state = state;
    this.obj = obj;
    this.metadata = obj.getMetadata();
  }

  @Override
  public USLObject deref(final Map<String, Object> env) {
    this.obj = this.obj.deref(env);
    return this;
  }

  @Override
  public int innerCompare(final USLObject other, final Map<String, Object> env) {
    return this.obj.innerCompare(other, env);
  }

  @Override
  public AviatorType getAviatorType() {
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
  public USLObject match(final USLObject other, final Map<String, Object> env) {
    return this.obj.match(other, env);
  }

  @Override
  public USLObject neg(final Map<String, Object> env) {
    return this.obj.neg(env);
  }

  @Override
  public USLObject setValue(final USLObject value, final Map<String, Object> env) {
    return this.obj.setValue(value, env);
  }

  @Override
  public USLObject not(final Map<String, Object> env) {
    return this.obj.not(env);
  }

  @Override
  public String desc(final Map<String, Object> env) {
    return this.obj.desc(env);
  }

  @Override
  public USLObject add(final USLObject other, final Map<String, Object> env) {
    return this.obj.add(other, env);
  }

  @Override
  public USLObject bitAnd(final USLObject other, final Map<String, Object> env) {
    return this.obj.bitAnd(other, env);
  }

  @Override
  public USLObject bitOr(final USLObject other, final Map<String, Object> env) {
    return this.obj.bitOr(other, env);
  }

  @Override
  public USLObject bitXor(final USLObject other, final Map<String, Object> env) {
    return this.obj.bitXor(other, env);
  }

  @Override
  public USLObject shiftRight(final USLObject other, final Map<String, Object> env) {
    return this.obj.shiftRight(other, env);
  }

  @Override
  public boolean equals(final Object obj) {
    return this.obj.equals(obj);
  }

  @Override
  public USLObject shiftLeft(final USLObject other, final Map<String, Object> env) {
    return this.obj.shiftLeft(other, env);
  }

  @Override
  public USLObject unsignedShiftRight(final USLObject other,
                                      final Map<String, Object> env) {
    return this.obj.unsignedShiftRight(other, env);
  }

  @Override
  public USLObject bitNot(final Map<String, Object> env) {
    return this.obj.bitNot(env);
  }

  @Override
  public USLObject sub(final USLObject other, final Map<String, Object> env) {
    return this.obj.sub(other, env);
  }

  @Override
  public USLObject mod(final USLObject other, final Map<String, Object> env) {
    return this.obj.mod(other, env);
  }

  @Override
  public USLObject div(final USLObject other, final Map<String, Object> env) {
    return this.obj.div(other, env);
  }

  @Override
  public USLObject mult(final USLObject other, final Map<String, Object> env) {
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
  public USLObject getElement(final Map<String, Object> env, final USLObject indexObject) {
    return this.obj.getElement(env, indexObject);
  }

}
