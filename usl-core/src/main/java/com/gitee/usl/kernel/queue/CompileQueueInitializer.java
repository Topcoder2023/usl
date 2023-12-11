package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Notes;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.constant.ModuleConstant;
import com.gitee.usl.infra.thread.NamedThreadFactory;
import com.gitee.usl.infra.utils.AnnotatedComparator;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.QueueConfiguration;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.gitee.usl.infra.utils.EnabledLogger.info;

/**
 * 编译队列管理者
 * 存放 Disruptor 和 生产者的实例
 * 对外提供获取 Disruptor 和生产者实例的方法
 * 同时作为初始化器，会自动按配置初始化编译队列以及组装生产者
 * 组装生产者的顺序取决于生产者类上的 @Order 注解
 * <br/>
 * 编译队列为单一生产者模式
 * 多消费者独立消费模式
 * 即消费者之间互相独立消费同一编译事件，互不影响
 * 生产者必须实现 CompileConsumer 接口并通过 SPI 机制提供服务注册
 *
 * @author hongda.li
 */
@Notes(value = "编译队列初始化器",
        belongs = ModuleConstant.USL_CORE,
        viewUrl = "https://gitee.com/yixi-dlmu/usl/raw/master/usl-core/src/main/java/com/gitee/usl/kernel/queue/CompileQueueManager.java")
@Order
@AutoService(Initializer.class)
public class CompileQueueInitializer implements Initializer {
    private static final String THREAD_PREFIX = "Usl_Queue_Disruptor";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CompileEventProducer producer;
    private Disruptor<CompileEvent> disruptor;

    @SuppressWarnings("ReassignedVariable")
    @Override
    public void doInit(Configuration configuration) {
        QueueConfiguration config = configuration.configQueue();
        config.compileQueueInitializer(this);

        // 构造编译任务队列
        this.disruptor = new Disruptor<>(CompileEvent::new,
                config.getBufferSize(),
                new NamedThreadFactory(THREAD_PREFIX),
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

        // 设置单一生产者
        this.producer = new CompileEventProducer(this.disruptor.getRingBuffer());
        logger.info("设置脚本编译生产者 - [{}]", producer.getClass().getName());

        // 设置独立消费者
        // 每个消费者互相独立消费
        // 即前一个消费者消费事件后，后面的消费者仍可继续消费
        Iterator<Map.Entry<Integer, List<CompileConsumer>>> iterator = ServiceSearcher.searchAll(CompileConsumer.class)
                .stream()
                .collect(Collectors.groupingBy(consumer -> AnnotatedComparator.getOrder(consumer.getClass()), LinkedHashMap::new, Collectors.toList()))
                .entrySet()
                .iterator();

        Map.Entry<Integer, List<CompileConsumer>> first = iterator.next();

        // 设置首个消费者组
        // 如果存在多个同序消费者，则同时消费
        EventHandlerGroup<CompileEvent> eventsWith = disruptor.handleEventsWith(first.getValue().toArray(new CompileConsumer[]{}));
        Supplier<Object[]> supplier = () -> new Object[]{this.getConsumerNames(first.getValue())};
        info(logger, "设置脚本编译消费者 - {}", supplier);

        // 依次按序添加其余消费者组
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<CompileConsumer>> next = iterator.next();
            eventsWith = eventsWith.then(next.getValue().toArray(new CompileConsumer[]{}));
            Supplier<Object[]> supplierNext = () -> new Object[]{this.getConsumerNames(next.getValue())};
            info(logger, "设置脚本编译消费者 - {}", supplierNext);
        }

        // 启动编译任务队列
        this.disruptor.start();
    }

    /**
     * 获取消费者集合的名称
     *
     * @param consumers 消费者集合
     * @return 名称集合
     */
    private List<String> getConsumerNames(List<CompileConsumer> consumers) {
        return consumers.stream()
                .map(consumer -> consumer.getClass().getName())
                .collect(Collectors.toList());
    }

    public CompileEventProducer producer() {
        return producer;
    }

    public Disruptor<CompileEvent> disruptor() {
        return disruptor;
    }
}
