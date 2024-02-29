package com.gitee.usl.infra;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.dialect.DriverNamePool;
import com.gitee.usl.domain.Database;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.infra.utils.LoggerHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.sqlite.core.NativeDB;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import static com.gitee.usl.infra.DatabaseConstant.*;
import static com.gitee.usl.infra.constant.StringConstant.DEFAULT_PATH;
import static com.gitee.usl.infra.constant.StringConstant.FILE_SPLIT;

/**
 * @author hongda.li
 */
@Slf4j
public class ConnectHelper {
    private static final StringMap<Database> DB_CACHE = new StringMap<>();

    private ConnectHelper() {
    }

    static {
        LoggerHelper.addLoggerFilter(LoggerFactory.getLogger(NativeDB.class), msg -> !StrUtil.containsAny(
                msg,
                "[SQLite EXEC] pragma",
                "[SQLite EXEC] begin;",
                "[SQLite EXEC] commit;"
        ));
    }

    public static Database connectSystem() {
        return connect(DEFAULT_DATABASE_NAME);
    }

    public static Database connect(String databaseName) {
        String path = DEFAULT_PATH + StringConstant.USL_NAME + FILE_SPLIT;
        return connect(path, databaseName);
    }

    public static Database connect(String pathName, String databaseName) {
        String path = CharSequenceUtil.addSuffixIfNot(pathName, FILE_SPLIT);
        String name = CharSequenceUtil.addSuffixIfNot(databaseName, DATABASE_SUFFIX);
        return newDatabase(path, name);
    }

    public static Database newDatabase(String path, String name) {
        return DB_CACHE.computeIfAbsent(path + name, key -> {
            synchronized (ConnectHelper.class) {
                log.debug("创建 [Sqlite] 数据源 - {}", key);
                FileUtil.mkdir(path);
                // 1.UTF-8编码格式；2.临时表数据存放到内存；3.读写完全并发执行；4.执行写入数据后，直接结束
                SQLiteConfig config = new SQLiteConfig();
                config.setEncoding(SQLiteConfig.Encoding.UTF_8);
                config.setTempStore(SQLiteConfig.TempStore.MEMORY);
                config.setJournalMode(SQLiteConfig.JournalMode.WAL);
                config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
                SQLiteDataSource source = new SQLiteConnectionPoolDataSource(config);
                source.setDatabaseName(name);
                source.setUrl(DatabaseConstant.SQLITE_URL_PREFIX + key);
                Db proxy = new Db(source, DriverNamePool.DRIVER_SQLLITE3);
                return new Database(proxy, key);
            }
        });
    }
}
