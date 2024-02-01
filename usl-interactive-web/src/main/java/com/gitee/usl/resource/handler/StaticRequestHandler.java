package com.gitee.usl.resource.handler;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.resource.api.WebHandler;
import org.smartboot.http.common.enums.HeaderNameEnum;
import org.smartboot.http.common.utils.Mimetypes;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.io.InputStream;

/**
 * 静态资源处理
 * 处理 classpath://static/ 下的静态资源
 *
 * @author hongda.li
 */
public class StaticRequestHandler implements WebHandler {
    public static final String NOT_FOUND_PAGE = "/usl/public/404";
    private static final String PATH_PREFIX = "/usl";
    private static final String PATH = PATH_PREFIX + "/**";
    private static final String DEFAULT_FILE_TYPE = ".html";

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        String suffix = request.getRequestURI().substring(PATH_PREFIX.length() + 1);

        String resourceName;
        if (CharSequenceUtil.contains(suffix, StrPool.DOT)) {
            resourceName = suffix;
        } else {
            resourceName = suffix + DEFAULT_FILE_TYPE;
        }

        String contentType = Mimetypes.getInstance().getMimetype(resourceName);
        response.setHeader(HeaderNameEnum.CONTENT_TYPE.getName(), contentType + StringConstant.CONTENT_TYPE_SUFFIX);

        InputStream stream = ResourceUtil.getStreamSafe(resourceName);

        if (stream == null) {
            // 资源不存在，则重定向至404页面
            this.redirect(NOT_FOUND_PAGE);
        } else {
            // 资源若存在，则输出到响应体
            this.writeToStream(stream);
        }
    }
}
