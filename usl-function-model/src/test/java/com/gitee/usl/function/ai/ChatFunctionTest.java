package com.gitee.usl.function.ai;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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