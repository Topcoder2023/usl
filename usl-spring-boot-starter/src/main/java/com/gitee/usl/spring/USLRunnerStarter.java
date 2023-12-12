package com.gitee.usl.spring;

import com.gitee.usl.USLRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * @author hongda.li
 */
public class USLRunnerStarter implements ApplicationListener<ApplicationReadyEvent> {
    private final List<USLRunner> runnerList;

    public USLRunnerStarter(List<USLRunner> runnerList) {
        this.runnerList = runnerList;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.runnerList.forEach(USLRunner::start);
    }
}
