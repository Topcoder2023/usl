package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.thread.NamedThreadFactory;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.QueueConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author hongda.li
 */
@Order
public class CompileQueueManager implements Initializer {
    private static final String THREAD_PREFIX = "Usl_Queue_Disruptor";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CompileEventProducer producer;
    private Disruptor<CompileEvent> disruptor;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        QueueConfiguration configuration = uslConfiguration.getQueueConfiguration();
        configuration.setCompileQueueManager(this);

        // 构造编译任务队列
        this.disruptor = new Disruptor<>(new CompileEventFactory(),
                configuration.getBufferSize(),
                new NamedThreadFactory(THREAD_PREFIX),
                ProducerType.SINGLE,
                new BusySpinWaitStrategy());

        // 设置单一生产者
        this.producer = new CompileEventProducer(this.disruptor.getRingBuffer());
        logger.info("Set compile queue producer => {}", producer.getClass().getName());

        // 设置独立消费者
        // 每个消费者互相独立消费
        // 即前一个消费者消费事件后，后面的消费者仍可继续消费
        Iterator<CompileConsumer> iterator = SpiServiceUtil.loadSortedService(CompileConsumer.class).iterator();
        CompileConsumer first = iterator.next();
        logger.info("Set compile queue consumer => {}", first.getClass().getName());
        EventHandlerGroup<CompileEvent> eventsWith = disruptor.handleEventsWith(first);
        while (iterator.hasNext()) {
            CompileConsumer next = iterator.next();
            eventsWith.then(next);
            logger.info("Set compile queue consumer => {}", next.getClass().getName());
        }

        // 启动编译任务队列
        this.disruptor.start();
    }

    public CompileEventProducer getProducer() {
        return producer;
    }

    public Disruptor<CompileEvent> getDisruptor() {
        return disruptor;
    }
}
