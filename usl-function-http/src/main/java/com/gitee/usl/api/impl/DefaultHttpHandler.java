package com.gitee.usl.api.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.domain.Returns;
import com.gitee.usl.infra.constant.NumberConstant;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

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

        List<WebRoute> routeList = Optional.ofNullable(mapping.get(path))
                .map(Stream::of)
                .orElse(mapping.entrySet()
                        .stream()
                        .filter(entry -> PATH_MATCHER.match(entry.getKey(), path))
                        .map(Map.Entry::getValue))
                .toList();

        List<WebRoute> filterList = routeList.stream()
                .filter(route -> route.getFilterFlag().isTrue())
                .toList();

        for (WebRoute filter : filterList) {
            boolean release = filter.doHandle(request, response);
            if (!release) {
                return;
            }
        }

        List<WebRoute> handlerList = routeList.stream()
                .filter(route -> route.getFilterFlag().isFalse())
                .toList();

        if (CollUtil.isEmpty(handlerList)) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            log.warn("未找到匹配的路由处理器 - [{}]", path);
            return;
        }

        if (handlerList.size() > NumberConstant.ONE) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            log.warn("存在重复的路由处理器 - [{}]", path);
            return;
        }

        try {
            log.debug("开始处理请求 - [{}]", path);
            handlerList.getFirst().doHandle(request, response);
        } catch (Exception e) {
            log.error("路由处理器发生异常", e);
            response.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
            response.write(JSON.toJSONString(Returns.failure(e.getMessage())).getBytes(StandardCharsets.UTF_8));
        }
    }
}
