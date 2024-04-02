package com.manda.usl.kernel.bean.event;

import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@Singleton
public class ReadyEventListener implements EventListener<ReadyEvent> {
    @Override
    public void listen(Event event) {
        Long start = USLRunner.getTimestamp();
        log.info("USL-Runner start in [{}]ms", (System.currentTimeMillis() - start));
    }
}
