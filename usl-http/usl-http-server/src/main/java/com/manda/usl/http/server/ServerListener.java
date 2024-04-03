package com.manda.usl.http.server;

import com.google.auto.service.AutoService;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.event.Event;
import com.manda.usl.kernel.bean.event.EventListener;
import com.manda.usl.kernel.bean.event.ReadyEvent;
import com.manda.usl.kernel.common.BeanLoader;
import com.manda.usl.kernel.common.SingleBeanLoader;

/**
 * @author hongda.li
 */
@Singleton
@AutoService(BeanLoader.class)
public class ServerListener implements EventListener<ReadyEvent>, SingleBeanLoader {

    @Override
    public void listen(Event event) {
        Server server = BeanContext.findFirst(Server.class);
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }

    @Override
    public Class<?> singleton() {
        return ServerListener.class;
    }
}
