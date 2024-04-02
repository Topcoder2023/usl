package com.manda.usl.kernel.common;

import java.util.Set;

/**
 * @author hongda.li
 */
public interface BeanLoader {
    /**
     * 扩展 Bean
     *
     * @return Bean 实例
     */
    Set<Class<?>> load();
}
