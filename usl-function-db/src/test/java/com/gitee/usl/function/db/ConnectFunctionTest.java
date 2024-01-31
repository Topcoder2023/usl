package com.gitee.usl.function.db;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    @Test
    void query() {
        USLConfiguration configuration = USLRunner.defaultConfiguration();
        Map<String, Object> custom = configuration.getCustomConfig();
        custom.put("database.url", "jdbc:mysql://localhost:3306");
        custom.put("database.user", "root");
        custom.put("database.password", "123456");
        custom.put("database.driver", "com.mysql.cj.jdbc.Driver");
        USLRunner runner = new USLRunner(configuration);
        runner.start();

        runner.run(new ResourceParam("connect.js"));
    }
}