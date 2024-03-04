package com.gitee.usl.function.data;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import com.gitee.usl.api.RegisterCallback;
import com.gitee.usl.api.annotation.ConditionOnTrue;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.ConnectHelper;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.gitee.usl.plugin.annotation.NotBlank;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

import static com.gitee.usl.infra.DatabaseConstant.*;

/**
 * 消息队列
 *
 * @author hongda.li, jiahao.song
 */
@Slf4j
@FunctionGroup
@ConditionOnTrue(ENABLE_MQ_KEY)
public class MessageQueueFunction implements RegisterCallback {

    @Function("produce_message")
    public void produceMessage(@NotBlank String message) {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            proxy.insert(Entity.create(TABLE_QUEUE_NAME)
                    .set(FIELD_MESSAGE_CONTENT, message)
                    .set(FIELD_PRODUCED_TIME, DateUtil.now()));
        } catch (SQLException e) {
            log.error("生产消息失败", e);
        }
    }

    @Function("consume_message")
    public void consumeMessages(Integer batchSize, Long consumeInterval) {
        if (batchSize == null) batchSize = DEFAULT_BATCH_SIZE; //默认单独消费
        if (consumeInterval == null) consumeInterval = DEFAULT_CONSUME_INTERVAL; //默认消费频率

        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            while (true) {
                List<Entity> messages = proxy.page(Entity.create(TABLE_QUEUE_NAME), new Page(0, batchSize));
                if (!messages.isEmpty()) {
                    for (Entity message : messages) {
                        //进行消费
                        proxy.del(Entity.create(TABLE_QUEUE_NAME).set("ID", message.getInt("ID")));
                        log.info("消费信息: {}", message.getStr(FIELD_MESSAGE_CONTENT));
                    }
                }else {
                    log.info("消费结束");
                    break;
                }
                // 按间隔消费
                Thread.sleep(consumeInterval);
            }
        } catch (SQLException | InterruptedException e) {
            log.error("消费信息失败", e);
        }
    }

    @Override
    public void callback(USLConfiguration configuration) {
        try {
            ConnectHelper.connect(DEFAULT_DATABASE_NAME)
                    .proxy()
                    .execute(ResourceUtil.readUtf8Str(TABLE_QUEUE_SQL));
        } catch (SQLException e) {
            log.error("消息队列表结构初始化失败", e);
            throw new USLException(e);
        }
    }
}