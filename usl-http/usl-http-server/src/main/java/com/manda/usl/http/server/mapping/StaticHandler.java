package com.manda.usl.http.server.mapping;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.common.enums.HeaderNameEnum;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.common.utils.Mimetypes;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
@Slf4j
public class StaticHandler extends HttpServerHandler {

    public static final String PREFIX = "/static";

    public static final String PATH = PREFIX + StrPool.SLASH + "**";

    private final String resource;

    private final String notFoundPage;

    private static final String DEFAULT_FILE_TYPE = ".html";

    public StaticHandler(String resource, String notFoundPage) {
        this.resource = resource;
        this.notFoundPage = notFoundPage;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String uri = request.getRequestURI();

        String suffix;
        try {
            suffix = uri.substring(PREFIX.length() + 1);
        } catch (Exception e) {
            log.debug("Static resource not found : {}", uri);
            response.setHttpStatus(HttpStatus.SEE_OTHER);
            response.setHeader(HeaderNameEnum.LOCATION.getName(), notFoundPage);
            return;
        }

        String resourceName;
        if (CharSequenceUtil.contains(suffix, StrPool.DOT)) {
            resourceName = resource + StrPool.SLASH + suffix;
        } else {
            resourceName = resource + StrPool.SLASH + suffix + DEFAULT_FILE_TYPE;
        }

        String contentType = Mimetypes.getInstance().getMimetype(resourceName);
        response.setHeader(HeaderNameEnum.CONTENT_TYPE.getName(), ContentType.build(contentType, StandardCharsets.UTF_8));
        InputStream stream = ResourceUtil.getStreamSafe(resourceName);

        if (stream == null) {
            // 资源不存在，则重定向至404页面
            log.debug("Static resource not found : {}", uri);
            response.setHttpStatus(HttpStatus.SEE_OTHER);
            response.setHeader(HeaderNameEnum.LOCATION.getName(), PREFIX + StrPool.SLASH + notFoundPage);
        } else {
            // 资源若存在，则输出到响应体
            IoUtil.copy(stream, response.getOutputStream());
        }
    }
}
