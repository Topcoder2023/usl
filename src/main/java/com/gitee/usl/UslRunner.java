package com.gitee.usl;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.exception.UslException;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * USL Runner 是 通用脚本语言执行器
 * 其实例可以存在多个，每个实例使用不同的配置
 *
 * @author hongda.li
 */
public class UslRunner {
    /**
     * USL Runner 名称前缀
     */
    private static final String USL_RUNNER_NAME_PREFIX = "USL Runner-";
    private static final Logger LOGGER = LoggerFactory.getLogger(UslRunner.class);

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
    private static final ThreadLocal<Map<String, UslRunner>> ENGINE_CONTEXT = ThreadLocal.withInitial(HashMap::new);

    private final String name;
    private final UslConfiguration configuration;

    public UslRunner() {
        this(defaultConfiguration());
    }

    public UslRunner(UslConfiguration configuration) {
        this.configuration = configuration;
        this.name = USL_RUNNER_NAME_PREFIX + NUMBER.getAndIncrement();
    }

    public void start() {
        Assert.isFalse(ENGINE_CONTEXT.get().containsKey(name), () -> new UslException("USL Runner has been started."));

        try {
            LOGGER.info("{} - Starting...", name);
            List<Initializer> initializers = ServiceSearcher.searchAll(Initializer.class);
            initializers.forEach(initializer -> initializer.doInit(configuration));
            LOGGER.info("{} - Start completed.", name);
            ENGINE_CONTEXT.get().put(name, this);
        } catch (Exception e) {
            ENGINE_CONTEXT.remove();
        }
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
                .map(UslConfiguration::getEngineConfiguration)
                .map(EngineConfiguration::getScriptEngineManager)
                .map(manager -> manager.<T>run(param))
                .orElseThrow(() -> new UslException("USL Runner has not been started."));
    }

    /**
     * 获取当前 USL 执行器的配置类
     *
     * @return USL 配置类
     */
    public UslConfiguration configuration() {
        return configuration;
    }

    /**
     * 获取默认的 USL 配置类
     *
     * @return USL 配置类
     */
    public static UslConfiguration defaultConfiguration() {
        return new UslConfiguration().configEngine(engine -> engine.scan(UslRunner.class));
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
     * 根据 USL Runner 名称获取实例
     *
     * @param name 名称
     * @return 实例
     */
    public static UslRunner findRunnerByName(String name) {
        return ENGINE_CONTEXT.get().get(name);
    }
}
