package com.gitee.usl.kernel.configure;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.queue.CompileQueueInitializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
@Description("消费队列配置类")
public class QueueConfig {

    @Description("USL配置类")
    private final Configuration configuration;

    @Description("编译队列初始大小")
    private int bufferSize = NumberConstant.EIGHT;

    @Description("脚本编译队列初始化器")
    private CompileQueueInitializer queueInitializer;

    public QueueConfig(Configuration configuration) {
        this.configuration = configuration;
    }

}
