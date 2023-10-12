package com.gitee.usl.app.plugin;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.engine.FunctionSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 缓存插件
 *
 * @author hongda.li
 */
public class CacheablePlugin implements BeginPlugin, SuccessPlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final long expired;
    private final DateUnit unit;
    private Object cache;
    private Date cacheTime;

    public CacheablePlugin(long expired, DateUnit unit) {
        this.expired = expired;
        this.unit = unit;
    }

    @Override
    public void onBegin(FunctionSession session) {
        // 缓存为空直接跳过
        if (cache == null || cacheTime == null) {
            logger.debug("Cache is empty. Try to re-execute and cache.");
            return;
        }

        // 缓存未设置永不过期且过期直接跳过
        boolean neverExpired = expired <= NumberConstant.ZERO;
        if (!neverExpired && DateUtil.between(cacheTime, new Date(), this.unit, true) > expired) {
            logger.debug("Cache is expired. Try to re-execute and cache.");
            this.cache = null;
            return;
        }

        // 直接将缓存的结果设置为本次执行结果
        session.setResult(cache);
    }

    @Override
    public void onSuccess(FunctionSession session) {
        if (this.cache != null) {
            return;
        }

        this.cacheTime = new Date();
        this.cache = session.result();
        logger.debug("Cache updated success.");
    }
}
