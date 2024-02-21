package com.gitee.usl.function.db;

import cn.hutool.core.io.resource.ResourceUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.function.domain.Database;
import com.gitee.usl.function.infra.DatabaseConstant;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    static USLRunner runner = new USLRunner()
            .configure(configuration -> configuration.put(DatabaseConstant.ENABLE_LOCK_KEY, true));

    @Test
    void connect() throws SQLException {
        runner.run(new Param("lock('test_obj4')"));
    }
}