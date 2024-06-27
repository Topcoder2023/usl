package com.manda.usl.kernel;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.digest.DigestUtil;
import com.googlecode.aviator.*;
import com.manda.usl.kernel.common.FuncEnhancer;
import com.manda.usl.kernel.function.EnhancedFunctionLoader;
import com.manda.usl.kernel.view.Param;
import lombok.Getter;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
@Getter
public final class USLRunner {
    /**
     * 默认的执行器名称
     */
    public static final String DEFAULT_NAME = "defaultRunner";

    private final String name;

    private final AviatorEvaluatorInstance aviator;

    private final EnhancedFunctionLoader enhancedFunctionLoader;

    private final List<FuncEnhancer> enhancers = new ArrayList<>();

    private boolean started = false;

    public USLRunner() {
        this(DEFAULT_NAME);
    }

    public USLRunner(String name) {
        this.name = name;
        this.aviator = AviatorEvaluator.newInstance();
        this.enhancedFunctionLoader = new EnhancedFunctionLoader(name, this.enhancers);
    }

    public synchronized void run() {
        Assert.isFalse(started, "USL-Runner has been started");
        this.configure();
        started = true;
    }

    private void configure() {
        this.aviator.useLRUExpressionCache(1 << 10);
        this.aviator.setOption(Options.TRACE_EVAL, false);
        this.aviator.setOption(Options.SERIALIZABLE, false);
        this.aviator.addFunctionLoader(this.enhancedFunctionLoader);
        this.aviator.setOption(Options.CAPTURE_FUNCTION_ARGS, true);
        this.aviator.setOption(Options.EVAL_MODE, EvalMode.INTERPRETER);
        this.aviator.setOption(Options.MAX_LOOP_COUNT, Integer.MIN_VALUE);
        this.aviator.setOption(Options.MATH_CONTEXT, MathContext.DECIMAL128);
        this.aviator.setOption(Options.NIL_WHEN_PROPERTY_NOT_FOUND, true);
        this.aviator.setOption(Options.ENABLE_PROPERTY_SYNTAX_SUGAR, true);
        this.aviator.setOption(Options.PUT_CAPTURING_GROUPS_INTO_ENV, false);
        this.aviator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);
        this.aviator.setOption(Options.ALLOWED_CLASS_SET, Collections.emptySet());
        this.aviator.setOption(Options.USE_USER_ENV_AS_TOP_ENV_DIRECTLY, true);
        this.aviator.removeFunctionLoader(ClassPathConfigFunctionLoader.getInstance());
        this.aviator.setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
        this.aviator.setOption(Options.ASSIGNABLE_ALLOWED_CLASS_SET, Collections.emptySet());
        this.aviator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
    }

    public USLRunner addEnhancer(FuncEnhancer funcEnhancer) {
        this.enhancers.add(funcEnhancer);
        return this;
    }

    /**
     * 执行表达式
     *
     * @param param 脚本参数
     * @return 执行结果
     */
    public Object execute(Param param) {
        String cacheKey = DigestUtil.md5Hex(param.getExpression());
        return this.aviator.execute(cacheKey, param.getExpression(), param.getContext(), param.isCached());
    }
}
