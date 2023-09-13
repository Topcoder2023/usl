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
        long sequence = ringBuffer.next();
        CompileEvent unpublished = ringBuffer.get(sequence)
                .setContent(expression)
                .setConfiguration(configuration);

        ringBuffer.publish(sequence);

        logger.debug("Publish compile event => [ID: {}, Sequence : {}, Expression : {}]",
                unpublished.getEventId(),
                sequence,
                unpublished.getContent());
    }
}
