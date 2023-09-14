package com.gitee.usl;

import cn.hutool.core.date.StopWatch;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.UslParam;
import com.gitee.usl.kernel.domain.UslResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * USL Runner 是 通用脚本语言执行器
 * 其实例可以存在多个，每个实例使用不同的配置
 *
 * @author hongda.li
 */
public class UslRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(UslRunner.class);

    private final UslConfiguration configuration;

    public UslRunner() {
        this(new UslConfiguration());
    }

    public UslRunner(UslConfiguration configuration) {
        this.configuration = configuration;

        final StopWatch watch = new StopWatch();
        watch.start();

        LOGGER.info("USL starting...");

        this.init(configuration);

        watch.stop();
        LOGGER.info("USL started finish with {} ms", watch.getTotalTimeMillis());
    }

    /**
     * 执行 USL 脚本
     *
     * @param uslParam USL 参数
     * @param <T>      USL 返回值泛型
     * @return USL 返回值
     */
    public <T> UslResult<T> run(UslParam uslParam) {
        return this.configuration.getEngineConfiguration()
                .getScriptEngine()
                .run(uslParam);
    }

    /**
     * 初始化 USL 执行器
     *
     * @param configuration USL 配置类
     */
    private void init(UslConfiguration configuration) {
        // 获取所有初始化器，并依次执行初始化动作
        List<Initializer> initializers = SpiServiceUtil.loadSortedService(Initializer.class);
        initializers.forEach(initializer -> initializer.doInit(configuration));
    }

    /**
     * 获取当前 USL 执行器的配置类
     *
     * @return USL 配置类
     */
    public UslConfiguration getConfiguration() {
        return configuration;
    }
}
