package com.manda.usl.http.client;

import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.event.Event;
import com.manda.usl.kernel.bean.event.EventListener;
import com.manda.usl.kernel.bean.event.PreparedEvent;

/**
 * @author hongda.li
 */
@Singleton
public class ClientExecutorFactoryListener implements EventListener<PreparedEvent> {
    @Override
    public void listen(Event event) {
        BeanContext.register(new ClientExecutorFactory());
    }
}
