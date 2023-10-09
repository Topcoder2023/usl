package com.gitee.usl.infra.thread;

import com.gitee.usl.api.Shutdown;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.google.auto.service.AutoService;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@AutoService(Shutdown.class)
@Order(Integer.MAX_VALUE - 100)
public class ExecutorShutdown implements Shutdown {
    private static final long TIMEOUT = 30L;

    @Override
    public void close(UslConfiguration configuration) {
        try (ThreadPoolExecutor executor = configuration.configThreadPool()
                .executorManager()
                .executor()) {
            executor.shutdown();

            if (!executor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ignored) {
            // Ignored exception
        }
    }
}
