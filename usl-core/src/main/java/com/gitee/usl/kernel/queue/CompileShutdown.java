package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.Shutdown;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@AutoService(Shutdown.class)
public class CompileShutdown implements Shutdown {
    @Override
    public void close(Configuration configuration) {
        configuration.configQueue()
                .compileQueueInitializer()
                .disruptor()
                .shutdown();
    }
}
