package com.gitee.usl.api;

import com.gitee.usl.kernel.configure.Configuration;

/**
 * @author hongda.li
 */
public interface Shutdown {
    /**
     * 在 JVM 关闭前的操作
     *
     * @param configuration 配置类
     */
    void close(Configuration configuration);
}
