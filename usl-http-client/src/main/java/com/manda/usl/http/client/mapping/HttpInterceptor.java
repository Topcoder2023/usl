package com.manda.usl.http.client.mapping;

import okhttp3.Interceptor;

/**
 * @author hongda.li
 */
public interface HttpInterceptor extends Interceptor {

    /**
     * 设置拦截器的顺序
     *
     * @return 优先级
     */
    default int order() {
        return 0;
    }
}
