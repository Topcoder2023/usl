package com.gitee.usl.function.http;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.domain.HttpServer;
import com.gitee.usl.domain.ExecutableParam;
import com.gitee.usl.domain.ResourceParam;
import com.gitee.usl.infra.structure.SharedSession;
import com.gitee.usl.infra.structure.StringMap;
import lombok.extern.slf4j.Slf4j;

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

    @Function("http_server")
    @SuppressWarnings("HttpUrlsUsage")
    public HttpServer server(HttpServer server) {
        log.info("HTTP嵌入式服务器启动成功 - [http://{}:{}]", server.getHost(), server.getPort());
        return server.start();
    }

    @Function("http_listen")
    public HttpServer server(int port) {
        return new HttpServer(port);
    }

    @Function("http_listen_host")
    public HttpServer server(int port, String host) {
        return new HttpServer(port, host);
    }

    @Function("http_route")
    public HttpServer handler(HttpServer server, String api, String resource) {
        USLRunner runner = SharedSession.getSession().getDefinition().getRunner();
        StringMap<WebRoute> handlerMap = server.getRouteMapping();
        log.info("HTTP嵌入式服务路由添加成功 - [{} ~ {}]", api, resource);
        handlerMap.put(api, (req, resp) -> {
            ExecutableParam param = new ExecutableParam(runner, new ResourceParam(resource));
            param.addContext(REQUEST_NAME, req);
            param.addContext(RESPONSE_NAME, resp);
            param.execute();
        });
        return server;
    }
}
