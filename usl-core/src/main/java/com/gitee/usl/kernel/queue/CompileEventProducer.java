package com.gitee.usl.kernel.queue;

import com.gitee.usl.kernel.configure.UslConfiguration;
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

    public void produce(String expression, UslConfiguration configuration) {
        this.ringBuffer.publishEvent((unpublished, sequence) -> {
            unpublished.setContent(expression);
            unpublished.setConfiguration(configuration);

            logger.debug("Publish compile event - [ID: {}, Sequence : {}]", unpublished.getEventId(), sequence);
        });
    }
}
