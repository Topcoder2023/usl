package com.gitee.usl.kernel.plugin;

/**
 * @author hongda.li
 */
public interface UslBeginPlugin extends UslPlugin {
    /**
     * 开始执行之前
     */
    void onBegin();
}
