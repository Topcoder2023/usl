package com.gitee.usl.function.ai;

import com.gitee.usl.USLRunner;
import com.gitee.usl.domain.Param;
import org.junit.jupiter.api.Test;

/**
 * @author hongda.li
 */
class ChatFunctionTest {

    static USLRunner runner = new USLRunner();

    @Test
    void chatFor100() {
        runner.run(new Param("chat_100('你是谁')"));
    }
}