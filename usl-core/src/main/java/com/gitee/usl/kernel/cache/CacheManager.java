package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Notes;
import com.gitee.usl.infra.constant.ModuleConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Notes(value = "缓存初始化器",
        belongs = ModuleConstant.USL_CORE,
        viewUrl = "https://gitee.com/yixi-dlmu/usl/raw/master/usl-core/src/main/java/com/gitee/usl/kernel/cache/CacheManager.java")
@AutoService(Initializer.class)
public class CacheManager implements Initializer {
    private ExpressionCache cache;

    @Override
    public void doInit(Configuration uslConfiguration) {
        ExpressionCache found = ServiceSearcher.searchFirst(ExpressionCache.class);
        if (found == null) {
            return;
        }

        this.cache = found;

        CacheConfiguration configuration = uslConfiguration.configCache();
        this.cache.init(configuration);
        configuration.setCacheManager(this);
    }

    public ExpressionCache cache() {
        return cache;
    }
}
