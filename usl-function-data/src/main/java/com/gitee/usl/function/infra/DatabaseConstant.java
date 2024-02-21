package com.gitee.usl.function.infra;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;

/**
 * @author hongda.li
 */
public class DatabaseConstant {
    private DatabaseConstant () {}

    public static final String FILE_SPLIT = SystemUtil.get(SystemUtil.FILE_SEPARATOR);

    public static final String DEFAULT_PATH = FileUtil.getUserHomePath() + FILE_SPLIT;

    public static final String DATABASE_SUFFIX = ".db";

    public static final String SQLITE_URL_PREFIX = "jdbc:sqlite:";

    public static final String DEFAULT_DATABASE_NAME = "SYSTEM";

    public static final String TABLE_LOCK_SQL = "sql/LOCK_TABLE.sql";

    public static final String TABLE_LOCK_NAME = "LOCK";

    public static final String TABLE_QUEUE_NAME = "QUEUE";

    public static final Long LOCK_TIMEOUT = 1000L * 60 * 5;

    public static final String ENABLE_MQ_KEY = "Enable_MQ";
    public static final String ENABLE_LOCK_KEY = "Enable_Lock";
    public static final String FIELD_NAME = "LOCK_NAME";
    public static final String FIELD_CREATED_TIME = "CREATED_TIME";
    public static final String FIELD_EXPIRED_TIME = "EXPIRED_TIME";
}
