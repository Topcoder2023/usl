package com.gitee.usl;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.Shutdown;
import com.gitee.usl.api.Interactive;
import com.gitee.usl.api.CliInteractive;
import com.gitee.usl.api.WebInteractive;
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
 * USL Runner 是 通用脚本语言执行器
 * 其实例可以存在多个，每个实例使用不同的配置
 *
 * @author hongda.li
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class USLRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(USLRunner.class);

    /**
     * USL Runner 实例的数量
     * 每实例化一个 USL Runner 时，都会对 NUMBER 自增
     * USL Runner 作为重量级对象，不建议实例化多个
     * 建议全局唯一或根据使用场景定制化
     */
    private static final AtomicInteger NUMBER = new AtomicInteger(NumberConstant.ONE);

    /**
     * USL Runner 实例全局缓存
     */
    private static final Map<String, USLRunner> ENGINE_CONTEXT = new ConcurrentHashMap<>(NumberConstant.EIGHT);

    /**
     * USL Runner的名称
     * 每一个执行器的名称应该唯一
     */
    private final String name;

    /**
     * 实例启动时间
     */
    private final Date startTime;

    /**
     * 在 JVM 关闭前的回调函数
     */
    private static List<Shutdown> shutdowns;

    /**
     * USL Runner的配置选项
     * 支持为每一个执行器设置单独的配置
     */
    private final Configuration configuration;

    static {
        // 绑定 JVM 生命周期钩子，在关闭 JVM 之前执行 USL 关闭回调函数
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

    /**
     * 根据默认配置类构造 USL 执行器
     */
    public USLRunner() {
        this(defaultConfiguration());
    }

    /**
     * 根据指定配置类构造 USL 执行器
     *
     * @param configuration 指定配置类
     */
    public USLRunner(Configuration configuration) {
        this(StringConstant.USL_RUNNER_NAME_PREFIX + NUMBER.getAndIncrement(), configuration);
    }

    /**
     * 根据指定配置类构造 USL 执行器
     *
     * @param configuration 指定配置类
     */
    public USLRunner(String name, Configuration configuration) {
        this.name = name;
        this.startTime = new Date();
        this.configuration = configuration;
        this.configuration.setRunner(this);
    }

    /**
     * 启动 USL 执行器
     * USL 执行器仅在启动后才能执行脚本
     * 默认不采用任何交互模式
     */
    public void start() {
        this.start(InteractiveMode.NONE);
    }

    /**
     * 启动 USL 执行器
     * 当交互模式为 WEB 时，会在启动后加载 WEB 服务
     * 当交互模式为 CLI 时，会在启动后开启命令行界面
     *
     * @param mode 指定的交互模式
     */
    public void start(InteractiveMode mode) {
        Assert.isFalse(ENGINE_CONTEXT.containsKey(name), () -> new UslException("USL Runner has been started."));

        try {
            LOGGER.info("{} - Starting...", name);
            ENGINE_CONTEXT.put(name, this);
            List<Initializer> initializers = ServiceSearcher.searchAll(Initializer.class);
            initializers.forEach(initializer -> initializer.doInit(configuration));
            LOGGER.info("{} - Start completed.", name);
        } catch (Exception e) {
            ENGINE_CONTEXT.values().removeIf(runner -> runner.equals(this));
            LOGGER.error("{} - Start failed.", name);
            throw e;
        }

        // 开启交互
        this.interactive(mode);
    }

    /**
     * 执行 USL 脚本
     *
     * @param param USL 参数
     * @param <T>   USL 返回值泛型
     * @return USL 返回值
     */
    public <T> Result<T> run(Param param) {
        return Optional.ofNullable(this.configuration)
                .map(Configuration::configEngine)
                .map(EngineConfiguration::scriptEngineManager)
                .map(manager -> manager.<T>run(param))
                .orElseThrow(() -> new UslException("USL Runner has not been started."));
    }

    /**
     * 获取当前 USL 执行器的配置类
     *
     * @return USL 配置类
     */
    public Configuration configuration() {
        return configuration;
    }

    /**
     * 获取默认的 USL 配置类
     *
     * @return USL 配置类
     */
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

    /**
     * 返回所有可用的函数实例
     *
     * @return 函数实例集合
     */
    public List<AviatorFunction> functions() {
        return Optional.ofNullable(this.configuration)
                .map(Configuration::configEngine)
                .map(EngineConfiguration::functionHolder)
                .map(FunctionHolder::toList)
                .orElse(Collections.emptyList());
    }

    /**
     * 获取当前 USL 执行器的名称
     *
     * @return USL 执行器名称
     */
    public String name() {
        return name;
    }

    /**
     * 获取当前 USL 执行器的启动时间
     *
     * @return 启动时间
     */
    public Date startTime() {
        return startTime;
    }

    /**
     * 根据 USL Runner 名称获取实例
     *
     * @param name 名称
     * @return 实例
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

    @Override
    public String toString() {
        return new StringJoiner(", ", USLRunner.class.getSimpleName() + "[", "]")
                .add(name)
                .toString();
    }
}
