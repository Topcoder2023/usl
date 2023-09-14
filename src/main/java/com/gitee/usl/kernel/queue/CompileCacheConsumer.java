package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.cache.UslCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * USL 缓存字节码 消费者
 * 优先级在字节码编译消费者之后
 *
 * @author hongda.li
 */
@Order(CompileGeneratorConsumer.GENERATOR_ORDER + 100)
public class CompileCacheConsumer implements CompileConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) throws Exception {
        final UslCache uslCache = event.getConfiguration()
                .getCacheConfiguration()
                .getUslCache();

        // 缓存脚本编译结果
        uslCache.insert(event.getKey(), event.getExpression());

        logger.debug("Cache expression => [Key : {}, Content : {}]", event.getKey(), event.getContent());
    }
}
