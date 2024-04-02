package com.manda.usl.kernel.bean.event;

import cn.hutool.core.util.TypeUtil;
import com.manda.usl.kernel.bean.BeanContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongda.li
 */
@Slf4j
public final class EventPublisher {
    private static final Map<Class<?>, EventListener<?>> LISTENER_MAP = new HashMap<>();

    private EventPublisher() {
    }

    public static void prepared() {
        BeanContext.find(EventListener.class).forEach(eventListener -> {
            Class<?> argument = (Class<?>) TypeUtil.getTypeArgument(eventListener.getClass());
            LISTENER_MAP.put(argument, eventListener);
            log.debug("Event-Listener found : [{} - {}]", eventListener.getClass().getName(), argument.getName());
        });
    }

    public static void publish(Event event) {
        LISTENER_MAP.entrySet()
                .stream()
                .filter(entry -> event.getClass().equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(eventListener -> {
                    try {
                        eventListener.listen(event);
                    } catch (Exception e) {
                        log.error("Exception occurred in the event listener", e);
                    }
                });
    }
}
