package com.gitee.usl.infra.thread;

import com.gitee.usl.api.Shutdown;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@AutoService(Shutdown.class)
@Order(Integer.MAX_VALUE - 100)
public class ExecutorShutdown implements Shutdown {

    @Description("线程池关闭等待超时时间")
    private static final long TIMEOUT = 30L;

    @Override
    public void close(Configuration configuration) {

        @Description("USL线程池")
        ThreadPoolExecutor executor = configuration.getExecutorConfig()
                .getPoolInitializer()
                .getExecutor();

        try {
            executor.shutdown();

            @Description("线程关闭成功标识")
            boolean success = executor.awaitTermination(TIMEOUT, TimeUnit.SECONDS);

            if (!success) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ignored) {
            // Ignored exception
        }
    }
}
