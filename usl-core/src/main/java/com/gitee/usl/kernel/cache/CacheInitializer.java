package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import lombok.Getter;

import java.util.Objects;

/**
 * @author hongda.li
 */
@Description("缓存初始化器，获取表达式缓存接口实现类，并执行初始化方法")
@Getter
@AutoService(Initializer.class)
public class CacheInitializer implements Initializer {

    @Description("表达式缓存接口")
    private ExpressionCache cache;

    @Override
    public void doInit(Configuration configuration) {
        ExpressionCache found = ServiceSearcher.searchFirst(ExpressionCache.class);

        this.cache = Objects.requireNonNullElseGet(found, CaffeineCache::new);

        this.cache.init(configuration.getCacheConfig().setCacheInitializer(this));
    }
}
