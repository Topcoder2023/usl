package com.gitee.usl.plugin.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.api.CacheKeyGenerator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.googlecode.aviator.utils.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 缓存插件
 *
 * @author hongda.li
 */
public class CacheablePlugin implements BeginPlugin, SuccessPlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Cache<String, Object> cache;
    private final CacheKeyGenerator cacheKeyGenerator;

    public CacheablePlugin(long maxSize,
                           long expired,
                           TimeUnit unit,
                           CacheKeyGenerator cacheKeyGenerator) {
        this.cache = Caffeine.newBuilder()
                .softValues()
                .maximumSize(maxSize)
                .expireAfterWrite(expired, unit)
                .build();
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    @Override
    public void onBegin(FunctionSession session) {
        String key = cacheKeyGenerator.generateKey(session.definition().name(), session.invocation());

        // 缓存为空直接跳过
        Object ifPresent = cache.getIfPresent(key);

        if (ifPresent == null) {
            logger.debug("Cache is empty. Try to update cache - [{}]", key);
            return;
        }

        // 直接将缓存的结果设置为本次执行结果
        session.setResult(ifPresent);
    }

    @Override
    public void onSuccess(FunctionSession session) {
        String key = cacheKeyGenerator.generateKey(session.definition().name(), session.invocation());

        if (cache.getIfPresent(key) != null) {
            return;
        }

        this.cache.put(key, session.result());
        logger.debug("Cache updated success - [{}]", key);
    }

    public static final class DefaultCacheKeyGenerator implements CacheKeyGenerator {
        @Override
        public String generateKey(String name, Invocation<?> invocation) {
            final StringBuilder builder = new StringBuilder(name);

            Object[] args = invocation.args();
            if (ArrayUtil.isEmpty(args) || args[NumberConstant.ZERO] instanceof Env) {
                return builder.toString();
            }

            return builder.append(StrPool.DASHED)
                    .append(Stream.of(args)
                            .filter(arg -> !Env.class.equals(arg.getClass()))
                            .map(String::valueOf)
                            .collect(Collectors.joining(StrPool.DASHED)))
                    .toString();
        }
    }
}
