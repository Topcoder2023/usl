package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.CompileProducer;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.structure.wrapper.ObjectWrapper;
import com.gitee.usl.infra.thread.NamedThreadFactory;
import com.gitee.usl.infra.utils.AnnotatedComparator;
import com.gitee.usl.infra.utils.LambdaHelper;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.QueueConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.gitee.usl.infra.utils.EnabledLogger.info;

/**
 * @author hongda.li
 */
@Order
@Getter
@Slf4j
@AutoService(Initializer.class)
@Description("编译队列初始化器")
public class CompileQueueInitializer implements Initializer {

    @Description("编译队列线程工厂")
    private static final NamedThreadFactory THREAD_FACTORY = new NamedThreadFactory("编译队列线程");

    @Description("阻塞策略")
    private static final BlockingWaitStrategy BLOCK_STRATEGY = new BlockingWaitStrategy();

    @Description("编译队列生产者")
    private CompileProducer producer;

    @Description("Disruptor实例")
    private Disruptor<CompileEvent> disruptor;

    @Override
    public void doInit(Configuration configuration) {

        @Description("编译队列配置类")
        QueueConfig config = configuration.getQueueConfig();

        config.setQueueInitializer(this);

        @Description("编译队列初始容量")
        int bufferSize = config.getBufferSize();

        this.disruptor = new Disruptor<>(CompileEvent::new, bufferSize, THREAD_FACTORY, ProducerType.SINGLE, BLOCK_STRATEGY);

        @Description("编译队列生产者")
        CompileProducer producer = ServiceSearcher.searchFirst(CompileProducer.class);
        this.producer = Objects.requireNonNullElseGet(producer, CompileEventProducer::new);

        log.info("编译队列生产者 - {}", this.producer.getClass().getName());
        this.producer.init(this.disruptor);

        this.initHandlers();

        this.disruptor.start();
    }

    @Description("初始化编译队列消费者组")
    private void initHandlers() {

        @Description("编译队列消费者")
        ObjectWrapper<EventHandlerGroup<CompileEvent>> handlerGroup = new ObjectWrapper<>();

        @Description("排序后的消费者")
        Map<Integer, List<CompileConsumer>> group = ServiceSearcher.searchAll(CompileConsumer.class)
                .stream()
                .collect(LambdaHelper.group(consumer -> AnnotatedComparator.getOrder(consumer.getClass())));

        group.values().forEach(consumers -> {
            CompileConsumer[] array = consumers.toArray(new CompileConsumer[]{});

            if (handlerGroup.isEmpty()) {
                handlerGroup.set(disruptor.handleEventsWith(array));
            } else {
                handlerGroup.set(handlerGroup.get().then(array));
            }

            info(log, "编译队列消费者 - {}", () -> consumers.stream().map(CompileConsumer::name).toArray());
        });
    }
}
