package com.gitee.usl.kernel.queue;

import com.gitee.usl.kernel.configure.Configuration;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * USL 编译队列 生产者
 *
 * @author hongda.li
 */
public class CompileEventProducer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RingBuffer<CompileEvent> ringBuffer;

    public CompileEventProducer(RingBuffer<CompileEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void produce(String expression, Configuration configuration) {
        this.ringBuffer.publishEvent((unpublished, sequence) -> {
            unpublished.setContent(expression);
            unpublished.setConfiguration(configuration);

            logger.debug("发布[脚本编译事件] - [事件ID: {}, 事件序列 : {}]", unpublished.getEventId(), sequence);
        });
    }
}
