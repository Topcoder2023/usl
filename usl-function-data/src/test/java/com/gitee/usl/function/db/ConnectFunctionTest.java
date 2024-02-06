package com.gitee.usl.function.db;

import cn.hutool.core.io.resource.ResourceUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.function.domain.Database;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    static USLRunner runner = new USLRunner();

    @Test
    void connect() throws SQLException {
        runner.run(new Param("db_lock('test_obj')"));
    }
}