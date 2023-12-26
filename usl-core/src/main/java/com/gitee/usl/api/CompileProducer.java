package com.gitee.usl.api;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.queue.CompileEvent;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * @author hongda.li
 */
@Description("USL编译队列生产者接口")
public interface CompileProducer {

    void init(Disruptor<CompileEvent> disruptor);

    void produce(String content, Configuration configuration);

}
