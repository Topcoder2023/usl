package com.gitee.usl.function.infra;

import cn.hutool.db.Db;
import cn.hutool.db.dialect.DriverNamePool;
import org.sqlite.SQLiteDataSource;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

/**
 * @author hongda.li
 */
public class ConnectHelper {
    private ConnectHelper() {
    }

    public static Db newDatabase(String path, String name) {
        SQLiteDataSource source = new SQLiteConnectionPoolDataSource();
        source.setDatabaseName(name + ".db");
        source.setUrl("jdbc:sqlite:" + path + source.getDatabaseName());
        return new Db(source, DriverNamePool.DRIVER_SQLLITE3);
    }
}
