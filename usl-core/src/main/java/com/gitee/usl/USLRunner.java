package com.gitee.usl;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.Shutdown;
import com.gitee.usl.api.Interactive;
import com.gitee.usl.api.CliInteractive;
import com.gitee.usl.api.WebInteractive;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.enums.InteractiveMode;
import com.gitee.usl.infra.exception.UslException;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongda.li
 */
@Description("USL-Runner通用脚本语言执行器")
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class USLRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(USLRunner.class);
    @Description("USL-Runner默认实例的数量，每默认实例化一个 USL-Runner 时，都会对此变量自增")
    private static final AtomicInteger NUMBER = new AtomicInteger(NumberConstant.ONE);

    @Description("USL-Runner实例全局缓存")
    private static final Map<String, USLRunner> ENGINE_CONTEXT = new ConcurrentHashMap<>(NumberConstant.EIGHT);

    @Description("USL-Runner的名称，每一个执行器的名称应该唯一")
    private final String name;

    @Description("USL-Runner实例启动时间")
    private final Date startTime;

    @Description("在JVM关闭前的回调函数")
    private static List<Shutdown> shutdowns;

    @Description("USL-Runner的配置选项，支持为每一个执行器设置单独的配置")
    private final Configuration configuration;

    static {
        bindShutdownHook();
    }

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
        Assert.isFalse(ENGINE_CONTEXT.containsKey(name), () -> new UslException("USL Runner has been started."));
        long start = System.currentTimeMillis();

        try {
            LOGGER.info("{} - 启动中...", name);
            ENGINE_CONTEXT.put(name, this);
            List<Initializer> initializers = ServiceSearcher.searchAll(Initializer.class);
            initializers.forEach(initializer -> initializer.doInit(configuration));
            LOGGER.info("{} - 启动成功，共耗时[{}]毫秒", name, (System.currentTimeMillis() - start));
        } catch (Exception e) {
            ENGINE_CONTEXT.values().removeIf(runner -> runner.equals(this));
            throw e;
        }

        // 开启交互
        this.interactive(mode);
    }

    @Description("执行脚本")
    public <T> Result<T> run(@Description("脚本参数") Param param) {
        return Optional.ofNullable(this.configuration)
                .map(Configuration::configEngine)
                .map(EngineConfiguration::scriptEngineManager)
                .map(manager -> manager.<T>run(param))
                .orElseThrow(() -> new UslException("USL Runner has not been started."));
    }

    @Description("获取当前USL-Runner执行器的配置类")
    public Configuration configuration() {
        return configuration;
    }

    @Description("获取新的默认配置")
    public static Configuration defaultConfiguration() {
        return new Configuration()
                .configEngine()
                .scan(USLRunner.class).finish()
                .configExecutor()
                .setCorePoolSize(8)
                .setMaxPoolSize(16)
                .setQueueSize(1024)
                .setAliveTime(60)
                .setAllowedTimeout(false)
                .setTimeUnit(TimeUnit.SECONDS).finish()
                .configWebServer()
                .setPort(10086)
                .setDebug(false)
                .finish();
    }

    @Description("返回所有可用的函数实例")
    public List<AviatorFunction> functions() {
        return Optional.ofNullable(this.configuration)
                .map(Configuration::configEngine)
                .map(EngineConfiguration::functionHolder)
                .map(FunctionHolder::toList)
                .orElse(Collections.emptyList());
    }

    @Description("获取当前USL-Runner执行器的名称")
    public String name() {
        return name;
    }

    @Description("获取当前USL-Runner执行器的启动时间")
    public Date startTime() {
        return startTime;
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
        final Interactive interactive;
        switch (mode) {
            case CLI:
                interactive = ServiceSearcher.searchFirst(CliInteractive.class);
                break;
            case WEB:
                interactive = ServiceSearcher.searchFirst(WebInteractive.class);
                break;
            case NONE:
            default:
                interactive = runner -> {
                };
                break;
        }

        Optional.ofNullable(interactive).ifPresent(item -> item.open(this));
    }

    @Description("绑定JVM生命周期钩子，在关闭JVM之前执行关闭回调函数")
    private static void bindShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 仅在关闭前再初始化回调函数
            shutdowns = ServiceSearcher.searchAll(Shutdown.class);
            // 若存在多个 USL 执行器实例则依次关闭
            ENGINE_CONTEXT.values().forEach(runner -> {
                LOGGER.info("{} - Shutdown initiated.", runner.name);
                shutdowns.forEach(shutdown -> shutdown.close(runner.configuration));
                LOGGER.info("{} - Shutdown completed.", runner.name);
            });
        }));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", USLRunner.class.getSimpleName() + "[", "]")
                .add(name)
                .toString();
    }
}
