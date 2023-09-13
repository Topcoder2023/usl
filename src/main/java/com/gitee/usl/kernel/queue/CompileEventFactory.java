package com.gitee.usl.kernel.queue;

import com.lmax.disruptor.EventFactory;

/**
 * @author hongda.li
 */
public class CompileEventFactory implements EventFactory<CompileEvent> {
    @Override
    public CompileEvent newInstance() {
        return new CompileEvent();
    }
}
