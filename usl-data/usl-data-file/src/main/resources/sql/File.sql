CREATE TABLE IF NOT EXISTS FILE
(
    -- 主键
    ID       INTEGER NOT NULL CONSTRAINT FILE_PK PRIMARY KEY AUTOINCREMENT,
    -- 文件大小
    SIZE     INTEGER,
    -- 文件名称
    NAME     REAL    NOT NULL,
    -- 文件路径
    PATH     TEXT    NOT NULL,
    -- 文件后缀
    SUFFIX   TEXT,
    -- 文件目录
    CATEGORY TEXT
);

