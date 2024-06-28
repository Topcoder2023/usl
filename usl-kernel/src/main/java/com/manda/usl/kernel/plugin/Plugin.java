package com.manda.usl.kernel.plugin;

/**
 * @author hongda.li
 */
public interface Plugin {

    /**
     * 插件生效顺序
     * 由低到高排列
     *
     * @return 生效顺序
     */
    default int order() {
        return 0;
    }
}
