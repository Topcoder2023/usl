package com.gitee.usl.function.db;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.db.Entity;
import com.gitee.usl.api.RegisterCallback;
import com.gitee.usl.api.annotation.ConditionOnTrue;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.gitee.usl.plugin.annotation.NotBlank;
import com.gitee.usl.plugin.annotation.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Date;

import static com.gitee.usl.function.infra.DatabaseConstant.*;

/**
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
@ConditionOnTrue(ENABLE_LOCK_KEY)
public class LockFunction implements RegisterCallback {

    @Function("db_lock")
    public void lock(@NotBlank String name) {
        this.lockWith(name, new Date(System.currentTimeMillis() + LOCK_TIMEOUT));
    }

    @Function("db_lock_with")
    public void lockWith(@NotBlank String name, @NotNull Date expiredTime) {
        try {
            new ConnectFunction()
                    .connect(DEFAULT_DATABASE_NAME)
                    .proxy()
                    .insert(Entity.create(TABLE_LOCK_NAME)
                            .set(FIELD_NAME, name)
                            .set(FIELD_CREATED_TIME, new Date())
                            .set(FIELD_EXPIRED_TIME, expiredTime));
        } catch (SQLException e) {
            log.error("加锁失败", e);
            throw new USLExecuteException(e);
        }
    }

    @Override
    public void callback(USLConfiguration configuration) {
        try {
            new ConnectFunction()
                    .connect(DEFAULT_DATABASE_NAME)
                    .proxy()
                    .execute(ResourceUtil.readUtf8Str(TABLE_LOCK_SQL));
        } catch (SQLException e) {
            log.error("分布式表结构初始化失败", e);
            throw new USLException(e);
        }
    }
}
