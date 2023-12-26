package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.Shutdown;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@AutoService(Shutdown.class)
public class CompileShutdown implements Shutdown {
    @Override
    public void close(Configuration configuration) {
        log.debug("开始关闭编译队列");

        configuration.getQueueConfig()
                .getQueueInitializer()
                .getDisruptor()
                .shutdown();
    }
}
