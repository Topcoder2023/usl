package com.manda.usl.kernel.common;

import java.util.Collections;
import java.util.Set;

/**
 * @author hongda.li
 */
public interface SingleBeanLoader extends BeanLoader {
    /**
     * 扩展 Bean
     *
     * @return Bean 实例
     */
    Class<?> singleton();

    @Override
    default Set<Class<?>> load() {
        return Collections.singleton(this.singleton());
    }
}
