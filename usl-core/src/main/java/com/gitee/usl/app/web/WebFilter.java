package com.gitee.usl.app.web;

import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface WebFilter {

    /**
     * 执行过滤逻辑
     *
     * @param request  请求
     * @param response 响应
     * @return 是否放行
     */
    boolean doFilter(HttpRequest request, HttpResponse response);
}
