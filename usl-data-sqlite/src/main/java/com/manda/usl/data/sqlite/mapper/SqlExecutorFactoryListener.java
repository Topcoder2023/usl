package com.manda.usl.data.sqlite.mapper;

import com.google.auto.service.AutoService;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.event.Event;
import com.manda.usl.kernel.bean.event.EventListener;
import com.manda.usl.kernel.bean.event.PreparedEvent;
import com.manda.usl.kernel.common.BeanLoader;
import com.manda.usl.kernel.common.SingleBeanLoader;

/**
 * @author hongda.li
 */
@Singleton
@AutoService(BeanLoader.class)
public class SqlExecutorFactoryListener implements EventListener<PreparedEvent>, SingleBeanLoader {
    @Override
    public void listen(Event event) {
        BeanContext.register(SqlExecutorFactory.defaultDataSource());
        BeanContext.register(new SqlExecutorFactory());
    }

    @Override
    public Class<?> singleton() {
        return SqlExecutorFactoryListener.class;
    }
}
