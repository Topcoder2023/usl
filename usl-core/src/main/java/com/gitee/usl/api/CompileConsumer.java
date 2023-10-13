package com.gitee.usl.api;

import com.gitee.usl.kernel.queue.CompileEvent;
import com.lmax.disruptor.EventHandler;

/**
 * USL 编译队列 消费者
 *
 * @author hongda.li
 */
public interface CompileConsumer extends EventHandler<CompileEvent> {
}
