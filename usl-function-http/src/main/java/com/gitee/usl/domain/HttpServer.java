package com.gitee.usl.domain;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.NetUtil;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.api.annotation.Accessible;
import com.gitee.usl.api.impl.DefaultHttpHandler;
import com.gitee.usl.infra.structure.StringMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
/**
 * @author hongda.li, jingshu.zeng
 */
@Slf4j
public class HttpServer {
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";

    @Getter
    private final int port;

    @Getter
    private final String host;

    @Getter
    private final String serverName;

    private final HttpBootstrap proxy;

    private final USLRunner runner;

    private final StringMap<WebRoute> routeMapping = new StringMap<>();

    public HttpServer(int port, USLRunner runner) {
        this(port, NetUtil.getLocalhostStr(), runner);
    }

    public HttpServer(int port, String host, USLRunner runner) {
        this.host = host;
        this.port = port;
        this.proxy = new HttpBootstrap();
        this.serverName = this.generateName();
        this.proxy.configuration()
                .host(host)
                .debug(false)
                .bannerEnabled(false)
                .serverName(serverName);
        this.proxy.setPort(port);
        this.runner = runner;
    }

    @Accessible
    public HttpServer start() {
        log.info("HTTP服务启动成功 - [{}]", serverName);
        this.proxy.httpHandler(new DefaultHttpHandler(routeMapping));
        this.proxy.start();
        return this;
    }

    @Accessible
    public void filter(String path, String resource) {
        log.info("HTTP服务过滤器添加成功 - [{} ~ {}]", path, resource);
        this.addRoute(path, resource, WebRoute::filter);
    }



    @Accessible
    public void handler(String path, String resource) {
        log.info("HTTP服务处理器添加成功 - [{} ~ {}]", path, resource);
        this.addRoute(path, resource, WebRoute::handler);
    }

    public void addRoute(String path,
                         String resource,
                         Consumer<WebRoute> consumer) {
        WebRoute route = new WebRoute() {
            @Override
            public Boolean doHandle(HttpRequest request, HttpResponse response) {
                ExecutableParam param = new ExecutableParam(runner, new ResourceParam(resource));

                // 解析URL参数
                Map<String, String> urlParams = parseUrlParams(request.getQueryString());
                param.addContext("urlParams", urlParams);

                // 解析JSON参数
                JSONObject jsonObj = parseJsonParams(request);
                param.addContext("jsonParams", jsonObj);

                // 解析Body参数
                Map<String, String> bodyParams = parseBodyParams(request);
                param.addContext("bodyParams", bodyParams);

                HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
                requestWrapper.set(request);
                param.addContext(REQUEST, requestWrapper);
                HttpResponseWrapper responseWrapper = new HttpResponseWrapper();
                responseWrapper.set(response);
                param.addContext(RESPONSE, responseWrapper);
                return Optional.ofNullable(param.execute())
                        .map(res -> Convert.toBool(res, Boolean.TRUE))
                        .orElse(Boolean.TRUE);
            }
        };
        consumer.accept(route);
        routeMapping.put(path, route);
    }

    private String generateName() {
        return this.host + ":" + this.port;
    }

    private Map<String, String> parseUrlParams(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isNotBlank(queryString)) {
            String decodedQuery = URLUtil.decode(queryString);
            for (String param : decodedQuery.split("&")) {
                String[] pair = param.split("=");
                String key = pair[0];
                String value = pair.length > 1 ? pair[1] : "";
                params.put(key, value);
            }
        }
        return params;
    }

    private JSONObject parseJsonParams(HttpRequest request) {
        JSONObject jsonObj = null;
        try {
            // 从请求体输入流中读取数据
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                String bodyString = IoUtil.read(inputStream, CharsetUtil.CHARSET_UTF_8);
                // 解析 JSON 字符串为 JSONObject 对象
                jsonObj = JSONUtil.parseObj(bodyString);
            }
        } catch (IOException e) {
            log.error("解析JSON参数时发生IO异常：", e);
        } catch (Exception e) {
            log.error("解析JSON参数时发生异常：", e);
        }
        return jsonObj;
    }

    private Map<String, String> parseBodyParams(HttpRequest request) {
        Map<String, String> bodyParams = new HashMap<>();
        String contentType = request.getContentType();
        try {
            if (StrUtil.isNotBlank(contentType) && contentType.toLowerCase().contains("application/json")) {
                // JSON格式的参数获取
                String bodyString = IoUtil.read(request.getInputStream(), CharsetUtil.CHARSET_UTF_8);
                JSONObject jsonObj = JSONUtil.parseObj(bodyString);
                for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
                    bodyParams.put(entry.getKey(), entry.getValue().toString());
                }
            } else if (StrUtil.isNotBlank(contentType) && contentType.toLowerCase().contains("application/x-www-form-urlencoded")) {
                // 表单形式的参数获取
                Map<String, String[]> params = request.getParameters();
                for (Map.Entry<String, String[]> entry : params.entrySet()) {
                    if (entry.getValue() != null && entry.getValue().length > 0) {
                        bodyParams.put(entry.getKey(), entry.getValue()[0]);
                    }
                }
            }
        } catch (IOException e) {
            log.error("解析请求的body参数时发生IO异常：", e);
        } catch (Exception e) {
            log.error("解析请求的body参数时发生异常：", e);
        }
        return bodyParams;
    }




}