package com.manda.usl.http.server.mapping;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson2.JSON;
import com.manda.usl.http.server.entity.Filter;
import com.manda.usl.http.server.entity.ServerMapping;
import com.manda.usl.http.server.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Slf4j
public class ServerHandler extends org.smartboot.http.server.HttpServerHandler {
    private final List<Filter> filterList;
    private final Map<String, ServerMapping> metaMap;
    private final StaticHandler staticHandler;

    public ServerHandler(StaticHandler staticHandler) {
        this.metaMap = ServerScanner.scanMapping();
        this.filterList = ServerScanner.scanFilter();
        this.staticHandler = staticHandler;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        ServerVariable.init(request, response);

        try {
            // Do filter before handle mapping
            for (Filter filter : filterList) {
                boolean doFilter = filter.doFilter(request, response);
                if (!doFilter) {
                    log.debug("Request has been intercepted by the filter");
                    return;
                }
            }

            // 静态资源匹配
            boolean isStatic = ServerMapping.getMatcher().match(StaticHandler.PATH, request.getRequestURI());
            if (isStatic) {
                staticHandler.handle(request, response);
                return;
            }

            final Result result;
            final ServerMapping match = this.match(request);

            if (match == null) {
                result = Result.failure(HttpStatus.NOT_FOUND.value());
                log.debug("No mapping for request : {}", request.getRequestURI());
            } else {
                if (match.matchMethod(request.getMethod())) {
                    result = match.invoke(request);
                } else {
                    result = Result.failure(HttpStatus.METHOD_NOT_ALLOWED.value());
                    log.debug("Not support method : [{} - {}]", request.getRequestURI(), request.getMethod());
                }
            }

            Optional.ofNullable(result).ifPresent(res -> {
                response.setHttpStatus(HttpStatus.valueOf(res.getCode()));
                response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
                try {
                    response.write(JSON.toJSONString(res).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    log.error("Error in write response", e);
                }
            });
        } finally {
            ServerVariable.remove();
        }
    }

    private ServerMapping match(HttpRequest request) {
        String uri = request.getRequestURI();

        return Optional.ofNullable(metaMap.get(uri)).orElse(metaMap.values()
                .stream()
                .filter(meta -> meta.matchUri(uri))
                .findFirst()
                .orElse(null));
    }
}
