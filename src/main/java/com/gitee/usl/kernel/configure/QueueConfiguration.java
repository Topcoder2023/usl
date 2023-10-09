package com.gitee.usl.kernel.configure;

import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.queue.CompileQueueManager;

/**
 * USL 消费队列配置类
 *
 * @author hongda.li
 */
public class QueueConfiguration {
    private final UslConfiguration configuration;
    private int bufferSize;
    private CompileQueueManager compileQueueManager;

    public QueueConfiguration(UslConfiguration configuration) {
        this.bufferSize = NumberConstant.EIGHT;
        this.configuration = configuration;
    }

    public UslConfiguration finish() {
        return this.configuration;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public QueueConfiguration setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public CompileQueueManager compileQueueManager() {
        return compileQueueManager;
    }

    public void compileQueueManager(CompileQueueManager compileQueueManager) {
        this.compileQueueManager = compileQueueManager;
    }
}
