package com.gitee.usl;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.*;
import com.gitee.usl.grammar.script.ES;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.enums.InteractiveMode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.kernel.enhancer.LoggerPluginEnhancer;
import com.gitee.usl.kernel.enhancer.ParameterBinderEnhancer;
import com.gitee.usl.kernel.loader.AnnotatedFunctionLoader;
import com.gitee.usl.kernel.loader.NativeFunctionLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * USL-Runner通用脚本语言执行器
 *
 * @author hongda.li
 */
@Slf4j
@Getter
public class USLRunner {

    /**
     * USL-Runner 默认实例的数量，每默认实例化一个 USL-Runner 时，都会对此变量自增
     */
    private static final AtomicInteger NUMBER = new AtomicInteger(NumberConstant.ONE);

    /**
     * USL-Runner 实例全局缓存
     */
    private static final Map<String, USLRunner> ENGINE_CONTEXT = new ConcurrentHashMap<>(NumberConstant.FOUR);

    /**
     * USL-Runner 的名称，每一个执行器的名称应该唯一
     */
    private final String name;

    /**
     * USL-Runner 实例启动时间
     */
    private Long timestamp;

    /**
     * USL-Runner 的配置选项，支持为每一个执行器设置单独的配置
     */
    private final USLConfiguration configuration;

    /**
     * 根据默认配置构造 USL-Runner 执行器
     */
    public USLRunner() {
        this(defaultConfiguration());
    }

    /**
     * 根据指定名称构造 USL-Runner 执行器
     *
     * @param name 执行器名称
     */
    public USLRunner(String name) {
        this(name, defaultConfiguration());
    }

    /**
     * 根据指定配置构造 USL-Runner 执行器
     *
     * @param configuration 执行器配置
     */
    public USLRunner(USLConfiguration configuration) {
        this(StringConstant.USL_RUNNER_NAME_PREFIX + NUMBER.getAndIncrement(), configuration);
    }

    /**
     * 根据指定名称和指定配置构造 USL-Runner 执行器
     *
     * @param name          执行器名称
     * @param configuration 执行器配置
     */
    public USLRunner(String name, USLConfiguration configuration) {
        this.name = name;
        this.configuration = configuration;
        this.configuration.setRunner(this);
    }

    /**
     * 启动 USL-Runner 执行器，执行器仅在启动后才能执行脚本，默认不采用任何交互模式
     */
    public void start() {
        this.start(InteractiveMode.NONE);
    }

    /**
     * 以指定的模式启动 USL-Runner 执行器
     *
     * @param mode 指定模式
     */
    public void start(InteractiveMode mode) {
        if (ENGINE_CONTEXT.containsKey(name)) {
            return;
        }

        log.info("{} - 启动中...", name);
        ENGINE_CONTEXT.put(name, this);

        this.timestamp = System.currentTimeMillis();

        try {
            this.configuration.refresh();
            log.info("{} - 启动成功，共耗时[{}]毫秒", this.name, (System.currentTimeMillis() - this.timestamp));
        } catch (Exception e) {
            ENGINE_CONTEXT.keySet().removeIf(name -> Objects.equals(name, this.name));
            throw e;
        }

        this.interactive(mode);
    }

    /**
     * 执行脚本
     *
     * @param param 脚本参数
     * @return 执行结果
     */
    public Result run(Param param) {
        Assert.isTrue(this.configuration.getRefreshed(), name + "尚未初始化");

        this.configuration.getCompiler().compile(param);

        if (param.getCompiled() instanceof ES es) {
            throw es.getException();
        }

        try {
            return Result.success(param.getCompiled().execute(param.getContext()));
        } catch (USLExecuteException uee) {
            log.warn("USL执行出现错误", uee);
            return Result.failure(uee.getResultCode(), uee.getMessage());
        }
    }

    /**
     * 获取当前 USL-Runner 执行器的配置类
     *
     * @return 配置类
     */
    public USLConfiguration configuration() {
        return configuration;
    }

    /**
     * 获取新的默认配置
     *
     * @return 默认配置
     */
    public static USLConfiguration defaultConfiguration() {
        return new USLConfiguration()
                .scan(USLRunner.class)
                .enhancer(new LoggerPluginEnhancer())
                .enhancer(new ParameterBinderEnhancer())
                .loader(new NativeFunctionLoader())
                .loader(new AnnotatedFunctionLoader());
    }

    /**
     * 返回所有可用的函数实例
     *
     * @return 函数实例列表
     */
    public List<_Function> functions() {
        return Optional.ofNullable(this.configuration)
                .map(USLConfiguration::getFunctionHolder)
                .map(FunctionHolder::toList)
                .orElse(Collections.emptyList());
    }

    /**
     * 根据 USL-Runner 执行器名称获取实例
     *
     * @param name 执行器名称
     * @return 执行器实例
     */
    public static USLRunner findRunnerByName(String name) {
        if (name == null) {
            return null;
        }
        return ENGINE_CONTEXT.get(name);
    }

    /**
     * 开启交互
     *
     * @param mode 交互模式
     */
    private void interactive(InteractiveMode mode) {
        final Interactive interactive = switch (mode) {
            case CLI -> ServiceSearcher.searchFirst(CliInteractive.class);
            case WEB -> ServiceSearcher.searchFirst(WebInteractive.class);
            default -> runner -> {
            };
        };

        Optional.ofNullable(interactive).ifPresent(item -> item.open(this));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", USLRunner.class.getSimpleName() + "[", "]")
                .add(name)
                .toString();
    }
}
