package com.gitee.usl.resource;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.api.WebInteractive;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.WebServerConfiguration;
import com.gitee.usl.resource.api.FilterRoute;
import com.gitee.usl.resource.api.WebFilter;
import com.gitee.usl.resource.api.WebHandler;
import com.gitee.usl.resource.api.WebHelper;
import com.gitee.usl.resource.entity.Returns;
import com.gitee.usl.resource.filter.SecurityFilter;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.common.utils.AntPathMatcher;
import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * B-S架构
 * 接收来自外部的请求
 * 采用内嵌式 WEB-SERVER 实现
 * 考虑到轻量级依赖，此处未选择 Tomcat、Undertow、Jetty 等，而是选择了 Smart-http-server
 *
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE)
@AutoService(WebInteractive.class)
public class WebInteractiveImpl extends HttpServerHandler implements WebInteractive {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private final String host;
    private final HttpBootstrap bootstrap;
    private final List<WebFilter> filterList;
    private final Map<String, WebHandler> handlerMap;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebInteractiveImpl() {
        this.bootstrap = new HttpBootstrap();
        this.host = NetUtil.getLocalhostStr();
        this.filterList = ServiceSearcher.searchAll(WebFilter.class);
        this.handlerMap = ServiceSearcher.searchAll(WebHandler.class)
                .stream()
                .collect(Collectors.toMap(WebHandler::getRoute, Function.identity()));
    }

    @Override
    public void open(USLRunner runner) {
        WebServerConfiguration config = runner.configuration().configWebServer();

        bootstrap.configuration()
                .host(host)
                .bannerEnabled(false)
                .debug(config.isDebug())
                .serverName(config.getName());

        bootstrap.httpHandler(new HttpServerHandler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response) throws Throwable {
                String uri = request.getRequestURI();
                if (uri == null) {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    return;
                }

                WebHandler handler = Optional.ofNullable(handlerMap.get(uri))
                        .orElse(handlerMap.entrySet()
                                .stream()
                                .filter(entry -> PATH_MATCHER.match(entry.getKey(), uri))
                                .findFirst()
                                .map(Map.Entry::getValue)
                                .orElse(null));

                if (handler == null) {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    return;
                }

                WebHelper.REQUEST_THREAD_LOCAL.set(request);
                WebHelper.RESPONSE_THREAD_LOCAL.set(response);

                try {
                    for (WebFilter filter : filterList) {
                        boolean release;

                        String[] routes = AnnotationUtil.getAnnotationValue(filter.getClass(), FilterRoute.class);

                        if (routes == null
                                || ArrayUtil.contains(routes, uri)
                                || Stream.of(routes).anyMatch(pattern -> PATH_MATCHER.match(pattern, uri))) {
                            release = filter.doFilter(request, response);
                        } else {
                            release = true;
                        }

                        if (!release) {
                            return;
                        }
                    }

                    handler.doHandle(request, response);
                } catch (Exception e) {
                    response.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
                    response.write(JsonKit.toJson(Returns.failure(e.getMessage())).getBytes(StandardCharsets.UTF_8));
                } finally {
                    WebHelper.remove();
                }
            }
        });

        bootstrap.setPort(config.getPort());
        bootstrap.start();

        logger.info("Web-Interactive start success - [{}://{}:{}]", "http", host, config.getPort() + SecurityFilter.LOGIN_PAGE);
    }
}
