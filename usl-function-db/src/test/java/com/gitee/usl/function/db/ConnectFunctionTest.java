package com.gitee.usl.function.db;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.Test;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    @Test
    void query() {
        USLConfiguration custom = USLRunner.defaultConfiguration();
        custom.put("database.url", "jdbc:mysql://localhost:3306");
        custom.put("database.user", "root");
        custom.put("database.password", "123456");
        custom.put("database.driver", "com.mysql.cj.jdbc.Driver");
        USLRunner runner = new USLRunner(custom);
        runner.start();

        runner.run(new ResourceParam("connect.js"));
    }
}