-- 创建函数元数据表结构
CREATE TABLE IF NOT EXISTS FUNCTION
(
    -- 主键自增ID
    ID              INTEGER NOT NULL CONSTRAINT FUNCTION_PK PRIMARY KEY AUTOINCREMENT CONSTRAINT FUNCTION_U1 UNIQUE,
    -- 执行器唯一名称
    RUNNER_NAME     TEXT    NOT NULL DEFAULT 'UNKNOWN',
    -- 函数名称
    FUNCTION_NAME   TEXT    NOT NULL,
    -- 函数别名(别名集合的JSON格式)
    ALIAS_NAME      TEXT,
    -- 函数元数据(元数据的JSON格式)
    ATTRIBUTE       TEXT,
    -- 函数所在类名
    CLASS_NAME      TEXT,
    -- 函数映射方法
    METHOD_NAME     TEXT,
    -- 函数插件链(插件全类名集合的JSON格式)
    PLUGIN_NAME     TEXT
);