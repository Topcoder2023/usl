package com.manda.usl.kernel;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.*;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.BeanScanner;
import com.manda.usl.kernel.common.BeanLoader;
import com.manda.usl.kernel.common.Configuration;
import com.manda.usl.kernel.common.SingleBeanLoader;
import com.manda.usl.kernel.function.EnhancedFunctionLoader;
import com.manda.usl.kernel.entity.Param;
import lombok.Getter;

import java.math.MathContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * @author hongda.li
 */
@AutoService(BeanLoader.class)
public final class USLRunner implements SingleBeanLoader {
    /**
     * 当前版本号
     */
    public static final String VERSION = "v1.1.0";
    /**
     * 配置文件存放地址
     * 相对路径 classpath://config.json
     */
    public static final String CONFIG_JSON_NAME = Optional.ofNullable(System.getProperty("config.path")).orElse("config.json");
    /**
     * 默认的执行器名称
     */
    public static final String DEFAULT_NAME = "defaultRunner";

    @Getter
    private static Class<?> main;

    @Getter
    private static Long timestamp;

    @Getter
    private static Configuration configuration;

    private static USLRunner defaultRunner;

    private static boolean started = false;

    @Getter
    private final String name;

    @Getter
    private final AviatorEvaluatorInstance aviator;

    @Getter
    private EnhancedFunctionLoader enhancedFunctionLoader;

    public USLRunner() {
        this.name = DEFAULT_NAME;
        this.aviator = AviatorEvaluator.getInstance();
    }

    public USLRunner(String name) {
        this.name = name;
        this.aviator = AviatorEvaluator.newInstance();
        this.enhancedFunctionLoader = new EnhancedFunctionLoader(name);
        this.configure();
    }

    public static USLRunner getInstance() {
        if (defaultRunner != null) {
            return defaultRunner;
        }
        defaultRunner = new USLRunner(DEFAULT_NAME);
        return defaultRunner;
    }

    public static void run() {
        Assert.isFalse(started, "USL-Runner has been started");

        USLRunner.main = CallerUtil.getCallerCaller();
        USLRunner.timestamp = System.currentTimeMillis();
        USLRunner.configuration = Configuration.parse(CONFIG_JSON_NAME);

        BeanContext.register(USLRunner.configuration);

        ServiceLoaderUtil.loadList(BeanLoader.class)
                .stream()
                .map(BeanLoader::load)
                .flatMap(Collection::stream)
                .distinct()
                .map(BeanScanner::new)
                .map(BeanScanner::scan)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(BeanContext::register);

        BeanContext.refresh();

        started = true;
    }

    @Override
    public Class<?> singleton() {
        return main;
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
        this.aviator.setOption(Options.ASSIGNABLE_ALLOWED_CLASS_SET, Collections.emptySet());
        this.aviator.setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
        this.aviator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
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
