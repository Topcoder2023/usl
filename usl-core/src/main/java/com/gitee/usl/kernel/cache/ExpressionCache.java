package com.gitee.usl.kernel.cache;


import com.gitee.usl.kernel.configure.CacheConfig;
import com.googlecode.aviator.Expression;

/**
 * @author hongda.li
 */
public interface ExpressionCache {
    /**
     * 初始化缓存
     *
     * @param configuration 缓存配置信息
     */
    void init(CacheConfig configuration);

    /**
     * 根据缓存键查询编译后的表达式
     *
     * @param key 缓存键
     * @return 编译后的表达式
     */
    CacheValue select(String key);

    /**
     * 根据缓存键移除编译后的表达式
     *
     * @param key 缓存键
     */
    void remove(String key);

    /**
     * 插入缓存
     *
     * @param key        缓存键
     * @param cacheValue 缓存编译后的表达式
     */
    void insert(String key, CacheValue cacheValue);

    /**
     * 输出快照
     * 此方法是为了更好的感知缓存变化
     * 如命中率、失败率等参数
     * 如无需输出快照则可不重写此方法
     */
    default void snapshot() {
    }
}
