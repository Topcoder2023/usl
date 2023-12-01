package com.gitee.usl.app.web;

import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface WebFilter {
    /**
     * 匹配的路由
     * 为空则接受全部路由
     *
     * @return 路由
     */
    default String accept() {
        return null;
    }

    /**
     * 执行过滤逻辑
     *
     * @param request  请求
     * @param response 响应
     * @return 是否放行
     */
    boolean doFilter(HttpRequest request, HttpResponse response);
}
