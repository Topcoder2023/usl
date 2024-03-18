package com.gitee.usl.infra;

import com.gitee.usl.plugin.annotation.Past;

/**
 * @author hongda.li
 */
public class DatabaseConstant {
    private DatabaseConstant () {}

    public static final String DATABASE_SUFFIX = ".db";

    public static final String SQLITE_URL_PREFIX = "jdbc:sqlite:";

    public static final String DEFAULT_DATABASE_NAME = "SYSTEM";

    public static final String TABLE_LOCK_SQL = "sql/LOCK_TABLE.sql";

    public static final String TABLE_QUEUE_SQL = "sql/QUEUE_TABLE.sql";

    public static final String TABLE_FUNCTION_SQL = "sql/FUNCTION_TABLE.sql";

    public static final String TABLE_INVOKE_SQL = "sql/INVOKE_TABLE.sql";

    public static final String TABLE_LOCK_NAME = "LOCK";

    public static final String TABLE_QUEUE_NAME = "QUEUE";

    public static final String TABLE_FUNCTION_NAME = "FUNCTION";

    public static final String TABLE_INVOKE_NAME = "INVOKE";

    public static final Long LOCK_TIMEOUT = 1000L * 60 * 5;

    public static final String ENABLE_MQ_KEY = "Enable_MQ";
    public static final String ENABLE_LOCK_KEY = "Enable_Lock";
    public static final String FIELD_NAME = "LOCK_NAME";
    public static final String FIELD_CREATED_TIME = "CREATED_TIME";
    public static final String FIELD_EXPIRED_TIME = "EXPIRED_TIME";
    public static final String FIELD_MESSAGE_CONTENT = "MESSAGE_CONTENT";
    public static final String FIELD_PRODUCED_TIME = "PRODUCED_TIME";
    public static final String FIELD_CONSUMED_TIME = "CONSUMED_TIME";
    public static final int DEFAULT_BATCH_SIZE = 1;
    public static final Long DEFAULT_CONSUME_INTERVAL = 1000L * 10;
}