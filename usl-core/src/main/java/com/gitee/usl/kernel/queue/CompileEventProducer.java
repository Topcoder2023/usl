package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.CompileProducer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@Description("USL编译队列生产者")
@AutoService(CompileEventProducer.class)
public class CompileEventProducer implements CompileProducer {

    @Description("Disruptor事件容器")
    private RingBuffer<CompileEvent> ringBuffer;

    @Override
    public void init(Disruptor<CompileEvent> disruptor) {
        this.ringBuffer = disruptor.getRingBuffer();
    }

    @Override
    public void produce(String content, Configuration configuration) {
        this.ringBuffer.publishEvent((unpublished, sequence) -> {
            unpublished.setContent(content);
            unpublished.setConfiguration(configuration);

            log.debug("发布脚本编译事件 - [事件ID : {}, 事件序列 : {}]\n{}", unpublished.getEventId(), sequence, content);
        });
    }
}
