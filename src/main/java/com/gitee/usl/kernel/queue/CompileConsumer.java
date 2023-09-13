package com.gitee.usl.kernel.queue;

import com.lmax.disruptor.EventHandler;

/**
 * USL 编译队列 消费者
 *
 * @author hongda.li
 */
public interface CompileConsumer extends EventHandler<CompileEvent> {
}
