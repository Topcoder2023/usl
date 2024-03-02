package com.gitee.usl.function.http;

import com.gitee.usl.api.WebRoute;
import com.gitee.usl.api.annotation.Accessible;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.domain.HttpRequestWrapper;
import com.gitee.usl.domain.HttpResponseWrapper;
import com.gitee.usl.domain.HttpServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
public class ServerFunction {
    @Accessible
    @Description("HTTP-Server")
    public static HttpServer server;

    @Accessible
    @Description({"USL-HTTP请求体变量", "存放当前请求的所有信息"})
    public static HttpRequestWrapper request;

    @Accessible
    @Description({"USL-HTTP响应体变量", "存放当前响应的所有信息"})
    public static HttpResponseWrapper response;

    @Function("http_listen")
    public HttpServer server(int port) {
        return new HttpServer(port);
    }

    @Function("http_listen_host")
    public HttpServer server(int port, String host) {
        return new HttpServer(port, host);
    }

    @Function("http_filter")
    public HttpServer filter(HttpServer server, String path, String resource) {
        log.info("HTTP嵌入式服务路由过滤器添加成功 - [{} ~ {}]", path, resource);
        return server.addRoute(server, path, resource, WebRoute::filter);
    }

    @Function("http_handler")
    public HttpServer handler(HttpServer server, String path, String resource) {
        log.info("HTTP嵌入式服务路由处理器添加成功 - [{} ~ {}]", path, resource);
        return server.addRoute(server, path, resource, WebRoute::handler);
    }
}
