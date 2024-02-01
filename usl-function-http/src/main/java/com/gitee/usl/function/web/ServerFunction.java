package com.gitee.usl.function.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.function.web.domain.HttpServer;
import com.gitee.usl.infra.structure.Script;
import com.gitee.usl.grammar.runtime.function.FunctionUtils;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.gitee.usl.grammar.runtime.function.FunctionUtils.wrapReturn;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ServerFunction {
    @Description({"USL-Http请求体变量", "存放当前请求的所有信息"})
    public static final String REQUEST_NAME = "Usl_Request";
    @Description({"USL-Http响应体变量", "存放当前响应的所有信息"})
    public static final String RESPONSE_NAME = "Usl_Response";

    @com.gitee.usl.api.annotation.Function("server")
    public HttpServer server(USLRunner runner, int port) {
        return new HttpServer(runner, port);
    }

    @com.gitee.usl.api.annotation.Function("server_start")
    public HttpServer start(HttpServer server) {
        server.getServer().start();
        return server;
    }

    @com.gitee.usl.api.annotation.Function("server_stop")
    public HttpServer stop(HttpServer server) {
        server.getServer().getRawServer().stop(Integer.MAX_VALUE);
        return server;
    }

    @com.gitee.usl.api.annotation.Function("server_filter")
    public HttpServer filter(Env env, HttpServer server, _Function function) {
        server.getServer().addFilter(new Filter() {
            @Override
            public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
                try (HttpServerRequest request = new HttpServerRequest(httpExchange); HttpServerResponse response = new HttpServerResponse(httpExchange)) {
                    _Object call = function.execute(env, wrapReturn(request), wrapReturn(response));
                    if (FunctionUtils.getBooleanValue(call, env)) {
                        chain.doFilter(httpExchange);
                    }
                }
            }

            @Override
            public String description() {
                return "USL-Filter_" + IdUtil.fastSimpleUUID();
            }
        });
        return server;
    }

    @com.gitee.usl.api.annotation.Function("server_resource")
    public HttpServer route(Env env, HttpServer server, String path) {
        server.getServer().setRoot(path);
        return server;
    }

    @com.gitee.usl.api.annotation.Function("server_route")
    public HttpServer route(Env env, HttpServer server, String path, _Function function) {
        server.getServer().addAction(path, (request, response) -> function.execute(env, wrapReturn(request), wrapReturn(response)));
        return server;
    }

    @com.gitee.usl.api.annotation.Function("server_route_script")
    public HttpServer route(Env env, HttpServer server, String path, Script script) {
        server.getServer().addAction(path, (request, response) -> {
            env.put(REQUEST_NAME, request);
            env.put(RESPONSE_NAME, response);
            script.run(env);
        });
        return server;
    }
}
