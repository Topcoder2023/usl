package com.manda.usl.kernel.function;

import com.google.auto.service.AutoService;
import com.manda.usl.kernel.USLRunner;
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
public class DefaultRunnerListener implements SingleBeanLoader, EventListener<PreparedEvent> {
    @Override
    public Class<?> singleton() {
        return DefaultRunnerListener.class;
    }

    @Override
    public void listen(Event event) {
        USLRunner runner = USLRunner.getInstance();
        BeanContext.register(runner.getName(), runner);
    }
}
