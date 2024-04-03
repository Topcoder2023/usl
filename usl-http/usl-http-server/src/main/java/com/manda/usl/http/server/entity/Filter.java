package com.manda.usl.http.server.entity;

import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
public interface Filter {
    /**
     * 优先级
     *
     * @return 优先级
     */
    default int order() {
        return 0;
    }

    /**
     * 执行过滤逻辑
     *
     * @param request  请求
     * @param response 响应
     * @return 是否允许放行
     */
    boolean doFilter(HttpRequest request, HttpResponse response);
}
