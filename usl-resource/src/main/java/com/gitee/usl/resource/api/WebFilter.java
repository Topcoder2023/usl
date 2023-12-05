package com.gitee.usl.resource.api;

import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
public interface WebFilter extends WebHelper {
    /**
     * 执行过滤逻辑
     *
     * @param request  请求
     * @param response 响应
     * @return 是否放行
     */
    boolean doFilter(HttpRequest request, HttpResponse response);
}
