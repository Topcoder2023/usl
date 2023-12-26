package com.gitee.usl.kernel.configure;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.cache.CacheInitializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.StringJoiner;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class CacheConfig {

    @Description("USL配置类")
    private final Configuration configuration;

    @Description("缓存初始化器")
    private CacheInitializer cacheInitializer;

    @Description("最大缓存容量")
    private int maxSize = 2 << 10;

    @Description("初始缓存容量")
    private int initSize = 2 << 4;

    @Description("访问后的失效时间")
    private Duration duration = Duration.ofHours(24L);

    public CacheConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public String snapshot() {
        return new StringJoiner(", ", "[", "]")
                .add("缓存最大容量=" + maxSize)
                .add("缓存初始容量=" + initSize)
                .add("缓存有效时间=" + duration)
                .toString();
    }
}
