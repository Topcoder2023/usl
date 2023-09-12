package com.gitee.usl.api;

/**
 * 环境变量加载器
 *
 * @author hongda.li
 */
public interface EnvLoader {
    /**
     * 根据环境变量的名称，加载对应环境变量的值
     *
     * @param name 环境变量的名称
     * @return 环境变量的值
     */
    String fetch(String name);
}
