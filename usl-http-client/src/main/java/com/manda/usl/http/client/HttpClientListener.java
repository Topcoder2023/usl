package com.manda.usl.http.client;

import com.google.auto.service.AutoService;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.common.BeanLoader;
import com.manda.usl.kernel.common.SingleBeanLoader;

/**
 * @author hongda.li
 */
@Singleton
@AutoService(BeanLoader.class)
public class HttpClientListener implements SingleBeanLoader {

    @Override
    public Class<?> singleton() {
        return HttpClientListener.class;
    }
}
