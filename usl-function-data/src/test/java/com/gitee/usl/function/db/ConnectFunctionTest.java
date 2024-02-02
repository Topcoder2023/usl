package com.gitee.usl.function.db;

import com.gitee.usl.USLRunner;
import com.gitee.usl.function.domain.Database;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    static USLRunner runner = new USLRunner();

    @Test
    void connect() {
        Object data = runner.run(new Param("db_connect('test')")).getData();
        Assertions.assertInstanceOf(Database.class, data);
    }
}