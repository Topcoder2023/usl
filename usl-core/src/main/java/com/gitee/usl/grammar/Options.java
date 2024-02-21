package com.gitee.usl.grammar;

import java.math.MathContext;
import java.util.Set;


/**
 * Aviator Evaluator Configuration options.
 *
 * @author dennis
 */
public enum Options {

    /**
     *
     */
    OPTIMIZE_LEVEL,

    /**
     * @see MathContext
     */
    MATH_CONTEXT,

    /**
     * When true, always parsing floating-point number into BigDecial, default is false.It replaces
     */
    ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL,
    /**
     * When true, always parsing integral number into BigDecial, default is false.
     *
     * @since 4.2.0
     */
    ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL,
    /**
     * Whether to trace expression evaluating procedure, default is false.
     */
    TRACE_EVAL,
    /**
     * Whether to put capturing groups into passed-in env map when regular-expression pattern matches,
     * default is true.If you don't want the groups, you can turn it off to get better performance in
     * regular-expression pattern matching.
     */
    PUT_CAPTURING_GROUPS_INTO_ENV,

    /**
     * Whether to capture the function arguments(at invocation) into env, the argument list will be
     * stored in __args__ variable in env valid for function body. Default is false(disabled).
     *
     * @since 4.2.0
     */
    CAPTURE_FUNCTION_ARGS,

    /**
     * Enable property access syntax sugar, use common-beantuils to access property such as "a.b.c"
     * etc. Default value is true, enable this behaviour.
     */
    ENABLE_PROPERTY_SYNTAX_SUGAR,

    /**
     * When enable property access syntax sugar, returns nil if the property value is not found or
     * throws exception.Default value is false,disabled this behaviour.
     */
    NIL_WHEN_PROPERTY_NOT_FOUND,

    /**
     * Whether to use user passed-in env as top level environment directly.If true, it may make side
     * effects(such as assignment) to user passed-in env., otherwise aviator will wrap the user
     * passed-in env and does not make any side effects into it.
     * <p>
     * Default is true.
     */
    USE_USER_ENV_AS_TOP_ENV_DIRECTLY,


    /**
     * Max loop count to prevent too much CPU consumption. If it's value is zero or negative, it means
     * no limitation on loop count.Default is zero.
     */
    MAX_LOOP_COUNT,
    /**
     * AviatorScript engine feature set, see {@link Feature}
     *
     * @since 5.
     */
    FEATURE_SET,

    /**
     * Allowed java class set in new statement and class's static method(fields) etc. It's null by
     * default. Null ALLOWED_CLASS_SET and ASSIGNABLE_ALLOWED_CLASS_SET means all classes are allowed
     * (default); Empty ALLOWED_CLASS_SET or ASSIGNABLE_ALLOWED_CLASS_SET means forbidding all
     * classes.
     *
     * @since 5.2.2
     */
    ALLOWED_CLASS_SET,

    /**
     * Allowed assignable java class set in new statement and class's static method(fields) etc. It's
     * null by default. Null ALLOWED_CLASS_SET and ASSIGNABLE_ALLOWED_CLASS_SET means all classes are
     * allowed (default); Empty ALLOWED_CLASS_SET or ASSIGNABLE_ALLOWED_CLASS_SET means forbidding all
     * classes.
     */
    ASSIGNABLE_ALLOWED_CLASS_SET,
    /**
     * Script engine evaluate mode, default is ASM mode.
     */
    EVAL_MODE,

    /**
     * Whether the compiled expression is serializable. If true, the compiled expression will
     */
    SERIALIZABLE;


    /**
     * The option's value union
     *
     * @author dennis
     */
    public static class Value {
        public boolean bool;
        public MathContext mathContext;
        public int number;
        public Set<Feature> featureSet;
        public Set<Class<?>> classes;
        public EvalMode evalMode;

        public Value(final EvalMode evalMode) {
            super();
            this.evalMode = evalMode;
        }

        public Value() {
            super();
        }

        static Value fromClasses(final Set<Class<?>> classes) {
            Value v = new Value();
            v.classes = classes;
            return v;
        }

        public Value(final Set<Feature> featureSet) {
            super();
            this.featureSet = featureSet;
        }

        public Value(final boolean bool) {
            super();
            this.bool = bool;
        }

        public Value(final MathContext mathContext) {
            super();
            this.mathContext = mathContext;
        }

        public Value(final int n) {
            super();
            this.number = n;
        }

        @Override
        public String toString() {
            return "Value [bool=" + this.bool + ", mathContext=" + this.mathContext + ", number="
                    + this.number + ", featureSet=" + this.featureSet + ", classes=" + this.classes + "]";
        }
    }

    /**
     * Cast value union into java object.
     *
     * @param val
     * @return
     */
    public Object intoObject(final Value val) {
        if (val == null) {
            return null;
        }
        switch (this) {
            case ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL:
            case ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL:
            case TRACE_EVAL:
            case PUT_CAPTURING_GROUPS_INTO_ENV:
            case ENABLE_PROPERTY_SYNTAX_SUGAR:
            case NIL_WHEN_PROPERTY_NOT_FOUND:
            case USE_USER_ENV_AS_TOP_ENV_DIRECTLY:
            case CAPTURE_FUNCTION_ARGS:
                return val.bool;
            case MAX_LOOP_COUNT:
            case OPTIMIZE_LEVEL:
                return val.number;
            case FEATURE_SET:
                return val.featureSet;
            case MATH_CONTEXT:
                return val.mathContext;
            case ALLOWED_CLASS_SET:
            case ASSIGNABLE_ALLOWED_CLASS_SET:
                return val.classes;
            case EVAL_MODE:
                return val.evalMode;
            case SERIALIZABLE:
                return val.bool;
        }
        throw new IllegalArgumentException("Fail to cast value " + val + " for option " + this);
    }

    /**
     * Cast java object into value union.
     *
     * @param val
     * @return
     */
    @SuppressWarnings("unchecked")
    public Value intoValue(final Object val) {
        return switch (this) {
            case ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL,
                    ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL,
                    TRACE_EVAL,
                    PUT_CAPTURING_GROUPS_INTO_ENV,
                    ENABLE_PROPERTY_SYNTAX_SUGAR,
                    NIL_WHEN_PROPERTY_NOT_FOUND,
                    USE_USER_ENV_AS_TOP_ENV_DIRECTLY,
                    CAPTURE_FUNCTION_ARGS,
                    SERIALIZABLE -> ((boolean) val) ? TRUE_VALUE : FALSE_VALUE;
            case OPTIMIZE_LEVEL -> EVAL_VALUE;
            case MAX_LOOP_COUNT -> new Value(((Number) val).intValue());
            case ALLOWED_CLASS_SET, ASSIGNABLE_ALLOWED_CLASS_SET -> Value.fromClasses((Set<Class<?>>) val);
            case FEATURE_SET -> new Value((Set<Feature>) val);
            case MATH_CONTEXT -> new Value((MathContext) val);
            case EVAL_MODE -> new Value((EvalMode) val);
        };
    }

    public boolean isValidValue(final Object val) {
        return switch (this) {
            case ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, TRACE_EVAL, PUT_CAPTURING_GROUPS_INTO_ENV, ENABLE_PROPERTY_SYNTAX_SUGAR, NIL_WHEN_PROPERTY_NOT_FOUND, USE_USER_ENV_AS_TOP_ENV_DIRECTLY, CAPTURE_FUNCTION_ARGS, SERIALIZABLE ->
                    val instanceof Boolean;
            case FEATURE_SET, ALLOWED_CLASS_SET, ASSIGNABLE_ALLOWED_CLASS_SET -> val instanceof Set;
            case OPTIMIZE_LEVEL -> {
                final int level = (Integer) val;
                yield level == 1 || level == 0;
            }
            case MAX_LOOP_COUNT -> val instanceof Long || val instanceof Integer;
            case MATH_CONTEXT -> val instanceof MathContext;
            case EVAL_MODE -> val instanceof EvalMode;
        };
    }

    public static final Value FALSE_VALUE = new Value(false);

    public static final Value TRUE_VALUE = new Value(true);

    public static final Value ZERO_VALUE = new Value(0);

    public static final Value DEFAULT_MATH_CONTEXT = new Value(MathContext.DECIMAL128);

    public static final Value EVAL_VALUE = new Value(1);

    private static final Value FULL_FEATURE_SET = new Value(Feature.getFullFeatures());
    private static final boolean TRACE_EVAL_DEFAULT_VAL =
            Boolean.parseBoolean(System.getProperty("aviator.trace_eval", "false"));

    public static final Value INTERPRETER_MODE = new Value(EvalMode.INTERPRETER);

    public static final Value NULL_CLASS_SET = Value.fromClasses(null);


    public Value getDefaultValueObject() {
        return switch (this) {
            case ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL,
                    ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL,
                    SERIALIZABLE,
                    NIL_WHEN_PROPERTY_NOT_FOUND,
                    CAPTURE_FUNCTION_ARGS -> FALSE_VALUE;
            case ENABLE_PROPERTY_SYNTAX_SUGAR,
                    PUT_CAPTURING_GROUPS_INTO_ENV,
                    USE_USER_ENV_AS_TOP_ENV_DIRECTLY -> TRUE_VALUE;
            case OPTIMIZE_LEVEL -> EVAL_VALUE;
            case MATH_CONTEXT -> DEFAULT_MATH_CONTEXT;
            case TRACE_EVAL -> TRACE_EVAL_DEFAULT_VAL ? TRUE_VALUE : FALSE_VALUE;
            case MAX_LOOP_COUNT -> ZERO_VALUE;
            case FEATURE_SET -> FULL_FEATURE_SET;
            case ALLOWED_CLASS_SET, ASSIGNABLE_ALLOWED_CLASS_SET -> NULL_CLASS_SET;
            case EVAL_MODE -> INTERPRETER_MODE;
        };
    }
}
