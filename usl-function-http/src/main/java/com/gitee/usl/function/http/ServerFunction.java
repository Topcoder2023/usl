package com.gitee.usl.function.http;

import com.alibaba.fastjson2.JSON;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.domain.HttpServer;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.USLException;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
public class ServerFunction {
    @Description({"USL-HTTP请求体变量", "存放当前请求的所有信息"})
    public static final String REQUEST_NAME = "Request";
    @Description({"USL-HTTP响应体变量", "存放当前响应的所有信息"})
    public static final String RESPONSE_NAME = "Response";

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

    @Function("http_server_start")
    public HttpServer startServer(HttpServer server) {
        log.info("HTTP嵌入式服务器启动成功 - [{}:{}]", server.getHost(), server.getPort());
        return server.start();
    }

    @Function("http_resp_string")
    public void write(HttpResponse response, String obj) {
        response.setContentType(HeaderValueEnum.DEFAULT_CONTENT_TYPE.getName());
        try {
            response.write(obj.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }

    @Function("http_resp_json")
    public void write(HttpResponse response, Object obj) {
        response.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
        try {
            response.write(JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }
}
