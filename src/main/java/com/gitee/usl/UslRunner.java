package com.gitee.usl;

import com.gitee.usl.api.UslInitializer;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.UslParam;
import com.gitee.usl.kernel.domain.UslResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * USL Runner 是 通用脚本语言执行器
 * 其实例可以存在多个，每个实例使用不同的配置
 *
 * @author hongda.li
 */
public class UslRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(UslRunner.class);

    /**
     * USL Runner 实例的数量
     * 每实例化一个 USL Runner 时，都会对 NUMBER 自增
     * USL Runner 作为重量级对象，不建议实例化多个
     * 建议全局唯一或根据使用场景定制化
     */
    private static final AtomicInteger NUMBER = new AtomicInteger(NumberConstant.ONE);

    private final UslConfiguration configuration;

    public UslRunner() {
        this(new UslConfiguration());
    }

    public UslRunner(UslConfiguration configuration) {
        this.configuration = configuration;
        int number = NUMBER.getAndIncrement();
        LOGGER.info("USL Runner-{} - Starting...", number);
        SpiServiceUtil.services(UslInitializer.class).forEach(initializer -> initializer.doInit(configuration));
        LOGGER.info("USL Runner-{} - Start completed.", number);
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
                .getScriptEngineManager()
                .run(uslParam);
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
