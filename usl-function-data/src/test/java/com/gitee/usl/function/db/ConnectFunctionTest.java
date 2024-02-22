package com.gitee.usl.function.db;

import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.DatabaseConstant;
import com.gitee.usl.domain.Param;
import com.gitee.usl.plugin.enhancer.FunctionMetaEnhancer;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.sqlite.core.NativeDB;

import java.sql.SQLException;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    static USLRunner runner = new USLRunner()
            .configure(configuration -> configuration.put(DatabaseConstant.ENABLE_LOCK_KEY, true))
            .configure(configuration -> configuration.enhancer(new FunctionMetaEnhancer()))
            .configure(configuration -> configuration.loggerLevel(NativeDB.class.getName(), Level.TRACE));

    public static void main(String[] args) {
        Object data = runner.run(new Param("date_value('02-2024-19','MM-yyyy-dd')")).getData();
    }
}