package com.gitee.usl.function.http;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Accessible;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.domain.HttpRequestWrapper;
import com.gitee.usl.domain.HttpResponseWrapper;
import com.gitee.usl.domain.HttpServer;
import com.gitee.usl.infra.structure.SharedSession;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
public class ServerFunction {
    @Accessible
    @Description("HTTP-Server")
    public static final HttpServer server = null;

    @Accessible
    @Description({"USL-HTTP请求体变量", "存放当前请求的所有信息"})
    public static final HttpRequestWrapper request = null;

    @Accessible
    @Description({"USL-HTTP响应体变量", "存放当前响应的所有信息"})
    public static final HttpResponseWrapper response = null;

    @Function("http_listen")
    public HttpServer server(int port) {
        USLRunner runner = SharedSession.getSession().getDefinition().getRunner();
        return new HttpServer(port, runner);
    }

    @Function("http_listen_host")
    public HttpServer server(int port, String host) {
        USLRunner runner = SharedSession.getSession().getDefinition().getRunner();
        return new HttpServer(port, host, runner);
    }
}
