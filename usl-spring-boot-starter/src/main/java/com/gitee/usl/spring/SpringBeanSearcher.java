package com.gitee.usl.spring;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.gitee.usl.api.ServiceFinder;
import com.google.auto.service.AutoService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda.li
 */
@AutoService(ServiceFinder.class)
public class SpringBeanSearcher implements ServiceFinder, ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public <T> List<T> findAll(Class<T> serviceType) {
        final List<T> serviceList = new ArrayList<>();

        Opt.ofEmptyAble(ServiceLoaderUtil.loadList(serviceType)).ifPresent(serviceList::addAll);
        Opt.ofEmptyAble(context.getBeansOfType(serviceType).values()).ifPresent(serviceList::addAll);

        return serviceList;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringBeanSearcher.context = context;
    }
}
