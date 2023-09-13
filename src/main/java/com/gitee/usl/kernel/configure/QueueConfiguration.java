package com.gitee.usl.kernel.configure;

import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.queue.CompileQueueManager;

/**
 * USL 消费队列配置类
 *
 * @author hongda.li
 */
public class QueueConfiguration {
    private int bufferSize;
    private CompileQueueManager compileQueueManager;

    public QueueConfiguration() {
        this.bufferSize = NumberConstant.EIGHT;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public QueueConfiguration setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public CompileQueueManager getCompileQueueManager() {
        return compileQueueManager;
    }

    public QueueConfiguration setCompileQueueManager(CompileQueueManager compileQueueManager) {
        this.compileQueueManager = compileQueueManager;
        return this;
    }
}
