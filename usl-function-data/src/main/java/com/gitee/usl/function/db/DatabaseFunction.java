package com.gitee.usl.function.db;

import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.function.domain.Database;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.plugin.annotation.NotBlank;
import com.gitee.usl.plugin.annotation.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
public class DatabaseFunction {

    @Function("db_execute")
    public void execute(@NotNull Database database, @NotBlank String sql) {
        try {
            database.proxy().execute(sql);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }
}
