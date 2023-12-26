package com.gitee.usl.api;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.queue.CompileEvent;
import com.lmax.disruptor.EventHandler;

/**
 * @author hongda.li
 */
@Description("USL编译队列消费者接口")
public interface CompileConsumer extends EventHandler<CompileEvent> {
    default String name() {
        return this.getClass().getName();
    }
}
