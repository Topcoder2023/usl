package com.gitee.usl.function.db;

import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.DatabaseConstant;
import com.gitee.usl.domain.Param;
import com.gitee.usl.plugin.enhancer.FunctionMetaEnhancer;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    static USLRunner runner = new USLRunner()
            .configure(configuration -> configuration.put(DatabaseConstant.ENABLE_LOCK_KEY, true))
            .configure(configuration -> configuration.enhancer(new FunctionMetaEnhancer()));

    @Test
    void connect() throws SQLException {
        runner.run(new Param("lock('test_obj5')"));
    }
}