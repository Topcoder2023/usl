package com.gitee.usl.kernel.configure;

import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.queue.CompileQueueInitializer;

/**
 * USL 消费队列配置类
 *
 * @author hongda.li
 */
public class QueueConfiguration {
    private final Configuration configuration;
    private int bufferSize;
    private CompileQueueInitializer compileQueueInitializer;

    public QueueConfiguration(Configuration configuration) {
        this.bufferSize = NumberConstant.EIGHT;
        this.configuration = configuration;
    }

    public Configuration finish() {
        return this.configuration;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public QueueConfiguration setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public CompileQueueInitializer compileQueueInitializer() {
        return compileQueueInitializer;
    }

    public void compileQueueInitializer(CompileQueueInitializer compileQueueInitializer) {
        this.compileQueueInitializer = compileQueueInitializer;
    }
}
