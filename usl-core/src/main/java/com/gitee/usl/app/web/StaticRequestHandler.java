package com.gitee.usl.app.web;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Optional;

/**
 * @author hongda.li
 */
@AutoService(AbstractWebHandler.class)
public class StaticRequestHandler extends AbstractWebHandler {
    private static final String PATH_PREFIX = "/static";
    private static final String PATH = PATH_PREFIX + "/*";
    private static final String DEFAULT_FILE_TYPE = ".html";

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String suffix = request.getRequestURI().substring(PATH_PREFIX.length() + 1);

        String resourceName;
        if (CharSequenceUtil.contains(suffix, StrPool.DOT)) {
            resourceName = suffix;
        } else {
            resourceName = suffix + DEFAULT_FILE_TYPE;
        }

        Optional.ofNullable(ResourceUtil.getStreamSafe(resourceName)).ifPresent(stream -> writeToStream(stream, response));
    }
}
