package com.gitee.usl.grammar;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.grammar.runtime.function.internal.*;
import com.gitee.usl.grammar.runtime.type.Function;
import com.gitee.usl.grammar.utils.IdentityHashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Syntax features.
 *
 * @author dennis(killme2008 @ gmaill.com)
 */
public enum Feature {


    /**
     * variable assignment
     */
    Assignment,
    /**
     * return statement
     */
    Return(asList(Singleton.get(ReducerReturnFunction.class))),
    /**
     * if/elsif/else statement
     */
    If(asList(Singleton.get(IfCallccFunction.class))),
    /**
     * for loop statement
     */
    ForLoop(asList(Singleton.get(ReducerFunction.class),
            Singleton.get(ReducerBreakFunction.class),
            Singleton.get(ReducerContFunction.class),
            Singleton.get(ReducerReturnFunction.class))),
    /**
     * while statement
     */
    WhileLoop(asList(Singleton.get(ReducerFunction.class),
            Singleton.get(ReducerBreakFunction.class),
            Singleton.get(ReducerContFunction.class),
            Singleton.get(ReducerReturnFunction.class))),
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
    ExceptionHandle,
    /**
     * new Class(arguments) to create an instance of special class with arguments.
     */
    NewInstance,
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
    Use,

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
    private List<Function> functions = Collections.emptyList();


    private Feature() {

    }

    private static List<Function> asList(final Function... args) {
        List<Function> ret = new ArrayList<>(args.length);
        for (Function f : args) {
            ret.add(f);
        }
        return ret;
    }

    private Feature(final Set<Feature> prequires) {
        this.prequires = prequires;
    }

    private Feature(final List<Function> funcs) {
        this.functions = funcs;
    }

    private Feature(final Set<Feature> prequires, final List<Function> funcs) {
        this.prequires = prequires;
        this.functions = funcs;
    }

    public List<Function> getFunctions() {
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
