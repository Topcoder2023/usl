package com.gitee.usl.function.db;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.db.Db;
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
import java.util.Map;

import static com.gitee.usl.function.infra.DatabaseConstant.*;

/**
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
@ConditionOnTrue(ENABLE_LOCK_KEY)
public class LockFunction implements RegisterCallback {

    @Function("lock_acquire")
    public Map<String, Object> acquire(@NotBlank String name) {
        Db proxy = new ConnectFunction().connect(DEFAULT_DATABASE_NAME).proxy();
        return this.acquire(proxy, name);
    }

    @Function("lock_release")
    public Map<String, Object> release(@NotBlank String name) {
        Db proxy = new ConnectFunction().connect(DEFAULT_DATABASE_NAME).proxy();
        return this.release(proxy, name);
    }

    @Function("lock")
    public void lock(@NotBlank String name) {
        this.lockWith(name, new Date(System.currentTimeMillis() + LOCK_TIMEOUT));
    }

    @Function("lock_with")
    public void lockWith(@NotBlank String name, @NotNull Date expiredTime) {
        Db proxy = new ConnectFunction().connect(DEFAULT_DATABASE_NAME).proxy();

        try {
            Entity entity = (Entity) this.acquire(proxy, name);
            Date begin = new Date();
            if (entity == null) {
                proxy.insert(Entity.create(TABLE_LOCK_NAME)
                        .set(FIELD_NAME, name)
                        .set(FIELD_CREATED_TIME, begin)
                        .set(FIELD_EXPIRED_TIME, expiredTime));
                return;
            }

            // 判断锁是否已经超时
            Date end = new Date(entity.getLong(FIELD_EXPIRED_TIME));
            boolean isExpired = DateUtil.between(begin, end, DateUnit.MS, false) < 0;
            if (isExpired) {
                // 超时先释放锁
                this.release(proxy, name);
                // 释放后再上锁
                this.lockWith(name, expiredTime);
            }
        } catch (SQLException e) {
            log.error("加锁失败", e);
            throw new USLExecuteException(e);
        }
    }

    private Map<String, Object> acquire(Db proxy, String name) {
        try {
            return proxy.queryOne("SELECT * FROM "
                    + TABLE_LOCK_NAME
                    + " WHERE "
                    + FIELD_NAME
                    + " = ?", name);
        } catch (SQLException e) {
            log.error("获取锁失败", e);
            throw new USLExecuteException(e);
        }
    }

    public Map<String, Object> release(Db proxy, String name) {
        try {
            // 释放锁之前再次获取锁，避免另一个线程已经释放过锁
            Map<String, Object> acquired = this.acquire(proxy, name);
            if (acquired == null) {
                return null;
            } else {
                proxy.del(TABLE_LOCK_NAME, FIELD_NAME, name);
                return acquired;
            }
        } catch (SQLException e) {
            log.error("释放锁失败", e);
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
