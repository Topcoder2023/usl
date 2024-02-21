package com.gitee.usl.function.data;

import com.gitee.usl.api.annotation.ConditionOnTrue;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.DatabaseConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息队列
 *
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
@ConditionOnTrue(DatabaseConstant.ENABLE_MQ_KEY)
public class MessageQueueFunction {
}
