CREATE TABLE IF NOT EXISTS LOG
(
    -- 主键
    ID       INTEGER NOT NULL CONSTRAINT LOG_PK PRIMARY KEY AUTOINCREMENT,
    -- 日志时间
    TIME     TEXT    NOT NULL,
    -- 日志类型
    TYPE     TEXT    NOT NULL,
    -- 日志级别
    LEVEL    TEXT    NOT NULL,
    -- 日志消息
    MESSAGE  TEXT    NOT NULL,
    -- 日志来源
    SOURCE   TEXT    NOT NULL
);

