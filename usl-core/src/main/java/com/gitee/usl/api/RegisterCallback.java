package com.gitee.usl.api;

import com.gitee.usl.kernel.engine.USLConfiguration;

/**
 * 注册回调接口
 *
 * @author hongda.li
 */
public interface RegisterCallback {

    /**
     * 回调方法
     */
    void callback(USLConfiguration configuration);
}
