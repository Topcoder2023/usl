package com.gitee.usl;

import com.gitee.usl.api.*;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.script.ES;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.enums.InteractiveMode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.grammar.runtime.type.AviatorFunction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongda.li
 */
@Slf4j
@Getter
@Description("USL-Runner通用脚本语言执行器")
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class USLRunner {

    @Description("USL-Runner默认实例的数量，每默认实例化一个 USL-Runner 时，都会对此变量自增")
    private static final AtomicInteger NUMBER = new AtomicInteger(NumberConstant.ONE);

    @Description("USL-Runner实例全局缓存")
    private static final Map<String, USLRunner> ENGINE_CONTEXT = new ConcurrentHashMap<>(NumberConstant.EIGHT);

    @Description("USL-Runner的名称，每一个执行器的名称应该唯一")
    private final String name;

    @Description("USL-Runner实例启动时间")
    private final Date startTime;

    @Description("USL-Runner的配置选项，支持为每一个执行器设置单独的配置")
    private final Configuration configuration;

    @Description("根据默认配置构造USL-Runner执行器")
    public USLRunner() {
        this(defaultConfiguration());
    }

    @Description("根据指定名称构造USL-Runner执行器")
    public USLRunner(@Description("执行器名称") String name) {
        this(name, defaultConfiguration());
    }

    @Description("根据指定配置构造USL-Runner执行器")
    public USLRunner(@Description("执行器配置") Configuration configuration) {
        this(StringConstant.USL_RUNNER_NAME_PREFIX + NUMBER.getAndIncrement(), configuration);
    }

    @Description("根据指定名称和指定配置构造USL-Runner执行器")
    public USLRunner(@Description("执行器名称") String name,
                     @Description("执行器配置") Configuration configuration) {
        this.name = name;
        this.startTime = new Date();
        this.configuration = configuration;
        this.configuration.setRunner(this);
    }

    @Description("启动USL-Runner执行器，执行器仅在启动后才能执行脚本，默认不采用任何交互模式")
    public void start() {
        this.start(InteractiveMode.NONE);
    }

    @Description("以指定的模式启动USL-Runner执行器")
    public void start(InteractiveMode mode) {
        if (ENGINE_CONTEXT.containsKey(name)) {
            return;
        }

        long start = System.currentTimeMillis();

        try {
            log.info("{} - 启动中...", name);
            ENGINE_CONTEXT.put(name, this);
            List<Initializer> initializers = ServiceSearcher.searchAll(Initializer.class);
            initializers.forEach(initializer -> initializer.doInit(configuration));
            log.info("{} - 启动成功，共耗时[{}]毫秒", name, (System.currentTimeMillis() - start));
        } catch (Exception e) {
            ENGINE_CONTEXT.values().removeIf(runner -> Objects.equals(runner.name, name));
            throw e;
        }

        this.interactive(mode);
    }

    @Description("执行脚本")
    public Result run(@Description("脚本参数") Param param) {

        configuration.getCacheConfig().getScriptCompiler().compile(param);

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

    @Description("获取当前USL-Runner执行器的配置类")
    public Configuration configuration() {
        return configuration;
    }

    @Description("获取新的默认配置")
    public static Configuration defaultConfiguration() {
        return new Configuration().getEngineConfig().scan(USLRunner.class).getConfiguration();
    }

    @Description("返回所有可用的函数实例")
    public List<AviatorFunction> functions() {
        return Optional.ofNullable(this.configuration)
                .map(Configuration::getEngineConfig)
                .map(EngineConfig::getFunctionHolder)
                .map(FunctionHolder::toList)
                .orElse(Collections.emptyList());
    }

    @Description("根据USL-Runner执行器名称获取实例")
    public static USLRunner findRunnerByName(String name) {
        if (name == null) {
            return null;
        }
        return ENGINE_CONTEXT.get(name);
    }

    @Description("开启交互")
    private void interactive(@Description("交互模式") InteractiveMode mode) {
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
