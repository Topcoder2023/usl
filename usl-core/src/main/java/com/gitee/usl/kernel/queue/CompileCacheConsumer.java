package com.gitee.usl.kernel.queue;

import cn.hutool.crypto.digest.DigestUtil;
import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.cache.CacheValue;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Description("USL缓存消费者")
@AutoService(CompileConsumer.class)
@Order(CompileGeneratorConsumer.GENERATOR_ORDER + 100)
public class CompileCacheConsumer implements CompileConsumer {

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) {

        @Description("唯一缓存键")
        String generated = DigestUtil.sha256Hex(event.getContent());

        @Description("缓存值")
        CacheValue cacheValue = CacheValue.of(event.getInitEnv(), event.getExpression());

        event.getConfiguration()
                .getCacheConfig()
                .getCacheInitializer()
                .getCache()
                .insert(generated, cacheValue);
    }

}
