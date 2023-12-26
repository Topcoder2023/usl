package com.gitee.usl.kernel.configure;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.queue.CompileQueueInitializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * USL 消费队列配置类
 *
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class QueueConfig {
    private final Configuration configuration;

    @Description("编译队列初始大小")
    private int bufferSize = NumberConstant.EIGHT;

    private CompileQueueInitializer queueInitializer;

    public QueueConfig(Configuration configuration) {
        this.configuration = configuration;
    }
}
