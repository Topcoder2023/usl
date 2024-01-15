package com.googlecode.aviator;

import com.googlecode.aviator.runtime.function.internal.*;
import com.gitee.usl.grammar.type.USLFunction;
import com.googlecode.aviator.utils.IdentityHashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Syntax features.
 *
 * @author dennis(killme2008@gmaill.com)
 *
 */
public enum Feature {


  /**
   * variable assignment
   */
  Assignment,
  /**
   * return statement
   */
  Return(asList(ReducerReturnFunction.INSTANCE)),
  /**
   * if/elsif/else statement
   */
  If(asList(IfCallccFunction.INSTANCE)),
  /**
   * for loop statement
   */
  ForLoop(asList(ReducerFunction.INSTANCE, ReducerBreakFunction.INSTANCE,
      ReducerContFunction.INSTANCE, ReducerReturnFunction.INSTANCE)),
  /**
   * while statement
   */
  WhileLoop(asList(ReducerFunction.INSTANCE, ReducerBreakFunction.INSTANCE,
      ReducerContFunction.INSTANCE, ReducerReturnFunction.INSTANCE)),
  /**
   * let statement
   */
  Let(asSet(Feature.Assignment)),
  /**
   * Lexical scope
   */
  LexicalScope,
  /**
   * lambda to define function
   */
  Lambda,
  /**
   * fn to define named function
   */
  Fn(asSet(Feature.Assignment, Feature.Lambda)),
  /**
   * Internal vars such as __env__, __instance__
   */
  InternalVars,
  /**
   * try..catch..finally and throw statement to handle exceptions.
   */
  ExceptionHandle(
      asList(TryCatchFunction.INSTANCE, CatchHandlerFunction.INSTANCE, ThrowFunction.INSTANCE)),
  /**
   * new Class(arguments) to create an instance of special class with arguments.
   */
  NewInstance(asList(NewInstanceFunction.INSTANCE)),
  /**
   * String interpolation.For example, "a = 'aviator'; 'hello #{a}'" to generate a string 'hello
   * aviator'
   */
  StringInterpolation(asSet(Feature.InternalVars)),
  /**
   * use package.class to import java classes into current context.
   *
   * @since 5.2.0
   */
  Use(asList(UseFunction.INSTANCE)),

  /**
   * Access java class's static fields by Class.FIELD
   *
   * @since 5.2.2
   */
  StaticFields,
  /**
   * Invoke java class's static methods by Class.method(..args)
   *
   * @since 5.2.2
   */
  StaticMethods;

  /**
   * Require feature sets for this feature.
   */
  private Set<Feature> prequires = Collections.emptySet();

  /**
   * Functions to support the feature.
   */
  private List<USLFunction> functions = Collections.emptyList();


  private Feature() {

  }

  private static List<USLFunction> asList(final USLFunction... args) {
    List<USLFunction> ret = new ArrayList<>(args.length);
    for (USLFunction f : args) {
      ret.add(f);
    }
    return ret;
  }

  private Feature(final Set<Feature> prequires) {
    this.prequires = prequires;
  }

  private Feature(final List<USLFunction> funcs) {
    this.functions = funcs;
  }

  private Feature(final Set<Feature> prequires, final List<USLFunction> funcs) {
    this.prequires = prequires;
    this.functions = funcs;
  }

  public List<USLFunction> getFunctions() {
    return this.functions;
  }

  /**
   * Create a feature set from arguments.
   *
   * @param args
   * @return feature set
   */
  public static Set<Feature> asSet(final Feature... args) {
    Set<Feature> set = new IdentityHashSet<>();
    for (Feature f : args) {
      set.addAll(f.prequires);
      set.add(f);
    }
    return set;
  }

  public Set<Feature> getPrequires() {
    return this.prequires;
  }


  /**
   * Returns the full feature set.
   *
   * @return
   */
  public static Set<Feature> getFullFeatures() {
    return asSet(Feature.values());
  }

  /**
   * Returns the feature set that is compatible with aviator early versions(before 5.0).
   *
   * @return
   */
  public static Set<Feature> getCompatibleFeatures() {
    return asSet(Feature.Assignment, Feature.Lambda, Feature.InternalVars);
  }

}
