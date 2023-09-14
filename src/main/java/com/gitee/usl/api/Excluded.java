package com.gitee.usl.api;

import java.util.List;

/**
 * 可排除的 SPI 服务
 *
 * @author hongda.li
 */
public interface Excluded {
    /**
     * 排除的目标类
     *
     * @return 目标类型集合
     */
    List<Class<?>> targets();
}
