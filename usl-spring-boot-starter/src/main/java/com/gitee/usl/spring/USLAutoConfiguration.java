package com.gitee.usl.spring;

import com.gitee.usl.Runner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author hongda.li
 */
@Configuration
public class USLAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Runner runner() {
        return new Runner();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanSearcher beanSearcher() {
        return new SpringBeanSearcher();
    }

    @Bean
    @ConditionalOnMissingBean
    public USLRunnerStarter runnerStarter(List<Runner> runnerList) {
        return new USLRunnerStarter(runnerList);
    }
}
