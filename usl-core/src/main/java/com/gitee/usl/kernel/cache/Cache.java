package com.gitee.usl.kernel.cache;


import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.googlecode.aviator.Expression;

/**
 * @author hongda.li
 */
public interface Cache {
    /**
     * 初始化缓存
     *
     * @param configuration 缓存配置信息
     */
    void init(CacheConfiguration configuration);

    /**
     * 根据缓存键查询编译后的表达式
     *
     * @param key 缓存键
     * @return 编译后的表达式
     */
    Expression select(String key);

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
     * @param expression 缓存编译后的表达式
     */
    void insert(String key, Expression expression);

    /**
     * 输出快照
     * 此方法是为了更好的感知缓存变化
     * 如命中率、失败率等参数
     * 如无需输出快照则可不重写此方法
     */
    default void snapshot() {
    }
}
