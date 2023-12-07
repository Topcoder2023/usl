package com.gitee.usl.function.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.function.web.domain.HttpServer;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.structure.Script;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.googlecode.aviator.runtime.function.FunctionUtils.wrapReturn;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ServerFunction {

    @Function("server")
    public HttpServer server(USLRunner runner, int port) {
        return new HttpServer(runner, port);
    }

    @Function("server_start")
    public HttpServer start(HttpServer server) {
        server.getServer().start();
        return server;
    }

    @Function("server_stop")
    public HttpServer stop(HttpServer server) {
        server.getServer().getRawServer().stop(Integer.MAX_VALUE);
        return server;
    }

    @Function("server_filter")
    public HttpServer filter(Env env, HttpServer server, AviatorFunction function) {
        server.getServer().addFilter(new Filter() {
            @Override
            public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
                try (HttpServerRequest request = new HttpServerRequest(httpExchange); HttpServerResponse response = new HttpServerResponse(httpExchange)) {
                    AviatorObject call = function.call(env, wrapReturn(request), wrapReturn(response));
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

    @Function("server_resource")
    public HttpServer route(Env env, HttpServer server, String path) {
        server.getServer().setRoot(path);
        return server;
    }

    @Function("server_route")
    public HttpServer route(Env env, HttpServer server, String path, AviatorFunction function) {
        server.getServer().addAction(path, (request, response) -> function.call(env, wrapReturn(request), wrapReturn(response)));
        return server;
    }

    @Function("server_route_script")
    public HttpServer route(Env env, HttpServer server, String path, Script script) {
        server.getServer().addAction(path, (request, response) -> {
            env.put(StringConstant.REQUEST_NAME, request);
            env.put(StringConstant.RESPONSE_NAME, response);
            script.run(env);
        });
        return server;
    }
}
