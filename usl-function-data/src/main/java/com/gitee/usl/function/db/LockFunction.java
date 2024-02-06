package com.gitee.usl.function.db;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.db.Entity;
import com.gitee.usl.api.RegisterCallback;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.function.infra.DatabaseConstant;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.infra.structure.SharedSession;
import com.gitee.usl.plugin.annotation.NotBlank;
import com.gitee.usl.plugin.annotation.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
public class LockFunction implements RegisterCallback {
    public static final String FIELD_NAME = "LOCK_NAME";
    public static final String FIELD_RUNNER = "RUNNER_NAME";
    public static final String FIELD_CREATED_TIME = "CREATED_TIME";
    public static final String FIELD_EXPIRED_TIME = "EXPIRED_TIME";

    @Function("db_lock")
    public void lock(@NotBlank String name) {
        this.lockWith(name, new Date(System.currentTimeMillis() + DatabaseConstant.LOCK_TIMEOUT));
    }

    @Function("db_lock_with")
    public void lockWith(@NotBlank String name, @NotNull Date expiredTime) {
        String runnerName = SharedSession.getSession().getDefinition().getRunner().getName();
        try {
            new ConnectFunction()
                    .connect(DatabaseConstant.DEFAULT_DATABASE_NAME)
                    .proxy()
                    .insert(Entity.create(DatabaseConstant.TABLE_LOCK_NAME)
                            .set(FIELD_NAME, name)
                            .set(FIELD_RUNNER, runnerName)
                            .set(FIELD_CREATED_TIME, new Date())
                            .set(FIELD_EXPIRED_TIME, expiredTime));
        } catch (SQLException e) {
            log.error("加锁失败", e);
            throw new USLExecuteException(e);
        }
    }

    @Override
    public void callback() {
        try {
            new ConnectFunction()
                    .connect(DatabaseConstant.DEFAULT_DATABASE_NAME)
                    .proxy()
                    .execute(ResourceUtil.readUtf8Str(DatabaseConstant.TABLE_LOCK_SQL));
        } catch (SQLException e) {
            log.error("分布式表结构初始化失败", e);
            throw new USLExecuteException(e);
        }
    }
}
