package com.manda.usl.kernel.bean.event;

/**
 * @author hongda.li
 */
public interface EventListener<E extends Event> {
    /**
     * 事件监听
     *
     * @param event 事件源
     */
    void listen(Event event);
}
