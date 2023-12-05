package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.ScriptCompileHelper;
import com.gitee.usl.kernel.cache.Cache;
import com.gitee.usl.kernel.engine.ScriptEngineManager;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * USL 缓存字节码 消费者
 * 优先级在字节码编译消费者之后
 *
 * @author hongda.li
 */
@Order(CompileGeneratorConsumer.GENERATOR_ORDER + 100)
@AutoService(CompileConsumer.class)
public class CompileCacheConsumer implements CompileConsumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) {
        final Cache cache = event.getConfiguration()
                .configCache()
                .cacheManager()
                .cache();

        // 生成唯一缓存键
        String generated = ScriptCompileHelper.generateKey(event.getContent());
        logger.debug("开始缓存脚本:\n唯一缓存键 : [{}]\n缓存内容 : \n{}", generated, event.getContent());

        // 缓存脚本编译结果
        cache.insert(generated, event.getExpression());
    }
}
