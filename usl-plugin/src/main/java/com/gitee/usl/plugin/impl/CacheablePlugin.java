package com.gitee.usl.plugin.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.api.CacheKeyGenerator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 缓存插件
 * 对于计算量大，调用频繁，但执行结果固定的函数，可以考虑使用缓存插件
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
        String key = cacheKeyGenerator.generateKey(session.getDefinition().getName(), session.getInvocation());

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
        String key = cacheKeyGenerator.generateKey(session.getDefinition().getName(), session.getInvocation());

        if (cache.getIfPresent(key) != null) {
            return;
        }

        this.cache.put(key, session.getResult());
        logger.debug("Cache updated success - [{}]", key);
    }

    public static final class DefaultCacheKeyGenerator implements CacheKeyGenerator {
        @Override
        public String generateKey(String name, Invocation<?> invocation) {
            Object[] args = invocation.args();
            if (ArrayUtil.isEmpty(args)) {
                return name;
            }

            final Object envIfIsNative = args[0];

            final String argsName = Stream.of(args)
                    .filter(arg -> !Env.class.equals(arg.getClass()))
                    .map(obj -> {
                        Object val;
                        if (obj instanceof AviatorObject && envIfIsNative instanceof Env) {
                            val = ((AviatorObject) obj).getValue((Env) envIfIsNative);
                        } else {
                            val = obj;
                        }
                        return String.valueOf(val);
                    })
                    .collect(Collectors.joining(StrPool.DASHED));

            return CharSequenceUtil.isEmpty(argsName) ? name : name + StrPool.DASHED + argsName;
        }
    }
}
