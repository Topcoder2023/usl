package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.ScriptCompileHelper;
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
        String generated = ScriptCompileHelper.generateKey(event.getContent());

        event.getConfiguration()
                .getCacheConfig()
                .getCacheInitializer()
                .getCache()
                .insert(generated, event.getExpression());
    }
}
