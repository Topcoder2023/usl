package com.gitee.usl.api.service;

import com.gitee.usl.api.EnvLoader;
import com.gitee.usl.api.annotation.Order;

/**
 * @author hongda.li
 */
@Order(SystemEnvLoader.SYSTEM_ENV_LOADER_ORDER)
public class SystemEnvLoader implements EnvLoader {
    /**
     * 系统环境变量加载器的顺序
     */
    public static final int SYSTEM_ENV_LOADER_ORDER = Integer.MIN_VALUE + 100;

    @Override
    public String fetch(String name) {
        // 首先尝试从系统环境变量中加载
        String fromEnv;
        if ((fromEnv = System.getenv(name)) != null) {
            return fromEnv;
        }

        /// 再次尝试从系统属性中加载
        String fromProperty;
        if ((fromProperty = System.getProperty(name)) != null) {
            return fromProperty;
        }

        // 若均无法加载则返回空
        return null;
    }
}
