package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.Shutdown;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@AutoService(Shutdown.class)
public class CompileShutdown implements Shutdown {
    @Override
    public void close(UslConfiguration configuration) {
        configuration.configQueue()
                .compileQueueManager()
                .disruptor()
                .shutdown();
    }
}
