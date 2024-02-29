package com.gitee.usl.api;

import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
public interface WebRoute {
    /**
     * 执行业务逻辑
     *
     * @param request  请求
     * @param response 响应
     */
    void doHandle(HttpRequest request, HttpResponse response);
}
