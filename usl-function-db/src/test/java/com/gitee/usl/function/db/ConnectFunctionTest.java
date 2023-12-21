package com.gitee.usl.function.db;

import com.gitee.usl.USLRunner;
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
        Configuration configuration = USLRunner.defaultConfiguration();
        Map<String, Object> custom = configuration.configCustom();
        custom.put("database.url", "jdbc:mysql://localhost:3306");
        custom.put("database.user", "root");
        custom.put("database.password", "123456");
        custom.put("database.driver", "com.mysql.cj.jdbc.Driver");
        USLRunner runner = new USLRunner(configuration);
        runner.start();

        runner.run(new ResourceParam("connect.js"));
    }
}