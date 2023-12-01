package com.gitee.usl.app.web;

import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
public interface WebHandler extends WebHelper {
    /**
     * 获取 WEB 路由
     *
     * @return 路由配置
     */
    String getRoute();

    /**
     * 执行业务逻辑
     *
     * @param request  请求
     * @param response 响应
     */
    void doHandle(HttpRequest request, HttpResponse response);
}
