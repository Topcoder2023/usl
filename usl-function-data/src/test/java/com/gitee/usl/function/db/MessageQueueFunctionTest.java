package com.gitee.usl.function.db;

import com.gitee.usl.USLRunner;
import com.gitee.usl.domain.Param;
import com.gitee.usl.infra.DatabaseConstant;
import com.gitee.usl.plugin.enhancer.FunctionMetaEnhancer;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.sqlite.core.NativeDB;

public class MessageQueueFunctionTest {
    static USLRunner runner = new USLRunner()
            .configure(configuration -> configuration.put(DatabaseConstant.ENABLE_LOCK_KEY, true))
            .configure(configuration -> configuration.put(DatabaseConstant.ENABLE_MQ_KEY, true))
            .configure(configuration -> configuration.enhancer(new FunctionMetaEnhancer()))
            .configure(configuration -> configuration.loggerLevel(NativeDB.class.getName(), Level.TRACE));

    @Test
    void mq_produce() {
        Object data = runner.run(new Param("produce_message('message1');" +
                "produce_message('message2');" +
                "produce_message('message3');")).getData();
    }

    @Test
    void mq_consume() {
        Object data = runner.run(new Param("consume_message(2)")).getData();
    }
}
