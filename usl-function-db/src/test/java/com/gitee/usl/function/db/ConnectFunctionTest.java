package com.gitee.usl.function.db;

import com.gitee.usl.Runner;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    @Test
    void query() {
        Configuration configuration = Runner.defaultConfiguration();
        Map<String, Object> custom = configuration.getCustomConfig();
        custom.put("database.url", "jdbc:mysql://localhost:3306");
        custom.put("database.user", "root");
        custom.put("database.password", "123456");
        custom.put("database.driver", "com.mysql.cj.jdbc.Driver");
        Runner runner = new Runner(configuration);
        runner.start();

        runner.run(new ResourceParam("connect.js"));
    }
}