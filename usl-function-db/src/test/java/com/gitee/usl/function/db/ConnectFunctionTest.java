package com.gitee.usl.function.db;

import cn.hutool.db.Db;
import com.gitee.usl.USLRunner;
import com.gitee.usl.function.infra.ConnectHelper;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * @author hongda.li
 */
class ConnectFunctionTest {

    @Test
    void query() throws SQLException {
        Db db = ConnectHelper.newDatabase("", "usl");
        String sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        db.execute(sql);
    }
}