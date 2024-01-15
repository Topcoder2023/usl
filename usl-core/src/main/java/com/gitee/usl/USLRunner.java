package com.gitee.usl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.Shutdown;
import com.gitee.usl.api.Interactive;
import com.gitee.usl.api.CliInteractive;
import com.gitee.usl.api.WebInteractive;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.script.ES;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.enums.InteractiveMode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.infra.structure.wrapper.ObjectWrapper;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.cache.CacheValue;
import com.gitee.usl.kernel.cache.ExpressionCache;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.grammar.runtime.type.AviatorFunction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongda.li
 */
@Slf4j
@Getter
@Description("USL-Runner通用脚本语言执行器")
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class USLRunner {

    @Description("在JVM关闭前的回调函数")
    private static List<Shutdown> shutdowns;

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

        @Description("缓存实例")
        ExpressionCache cache = configuration.getCacheConfig().getCacheInitializer().getCache();

        @Description("脚本内容")
        String content = param.getScript();

        @Description("表达式缓存键")
        String key = DigestUtil.sha256Hex(content);

        @Description("表达式编译值")
        ObjectWrapper<CacheValue> wrapper = new ObjectWrapper<>(cache.select(key));

        if (wrapper.isEmpty()) {
            configuration.getQueueConfig()
                    .getQueueInitializer()
                    .getProducer()
                    .produce(content, configuration);

            while (wrapper.isEmpty()) {
                ThreadUtil.sleep(NumberConstant.ONE, TimeUnit.NANOSECONDS);
                wrapper.set(cache.select(key));
            }

            if (!param.isCached()) {
                cache.remove(key);
            }
        }

        CacheValue value = wrapper.get();

        if (value.getScript() instanceof ES) {
            throw ((ES) value.getScript()).getException();
        }

        try {
            param.addContext(value.getInitEnv());
            Object result = value.getScript().execute(param.getContext());
            return Result.success(result);
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

    @Description("绑定JVM生命周期钩子，在关闭JVM之前执行关闭回调函数")
    private static void bindShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdowns = ServiceSearcher.searchAll(Shutdown.class);
            ENGINE_CONTEXT.values().forEach(runner -> {
                log.info("{} - 开始销毁执行器", runner.name);
                shutdowns.forEach(shutdown -> shutdown.close(runner.configuration));
                log.info("{} - 执行器销毁完成", runner.name);
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
