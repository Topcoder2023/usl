package com.gitee.usl.kernel.cache;


import com.googlecode.aviator.Expression;

/**
 * @author hongda.li
 */
public interface UslCache {
    /**
     * 根据缓存键查询编译后的表达式
     *
     * @param key 缓存键
     */
    void select(String key);

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
