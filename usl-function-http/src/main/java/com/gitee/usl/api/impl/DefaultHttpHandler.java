package com.gitee.usl.api.impl;

import com.alibaba.fastjson2.JSON;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.domain.Returns;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.structure.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.common.utils.AntPathMatcher;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Slf4j
public class DefaultHttpHandler extends HttpServerHandler {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private final StringMap<WebRoute> mapping;

    public DefaultHttpHandler(StringMap<WebRoute> mapping) {
        this.mapping = mapping;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws Throwable {
        String path = request.getRequestURI();
        if (path == null) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            return;
        }

        WebRoute route = Optional.ofNullable(mapping.get(path))
                .orElse(mapping.entrySet()
                        .stream()
                        .filter(entry -> PATH_MATCHER.match(entry.getKey(), path))
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElse(null));

        if (route == null) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            log.warn("未找到匹配的路由处理器 - [{}]", path);
            return;
        }

        try {
            route.doHandle(request, response);
        } catch (Exception e) {
            log.error("路由处理器发生异常", e);
            response.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
            response.write(JSON.toJSONString(Returns.failure(e.getMessage())).getBytes(StandardCharsets.UTF_8));
        }
    }
}
