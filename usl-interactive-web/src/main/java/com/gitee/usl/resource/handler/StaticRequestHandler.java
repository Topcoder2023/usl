package com.gitee.usl.resource.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.infra.constant.StringConstant;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.common.enums.HeaderNameEnum;
import org.smartboot.http.common.enums.HttpStatus;
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
@Slf4j
public class StaticRequestHandler extends WebRoute {
    public static final String NOT_FOUND_PAGE = "/usl/public/404";
    private static final String PATH_PREFIX = "/usl";

    public static final String PATH = PATH_PREFIX + "/**";
    private static final String DEFAULT_FILE_TYPE = ".html";

    @Override
    public Boolean doHandle(HttpRequest request, HttpResponse response) {
        String uri = request.getRequestURI();
        String suffix;
        try {
            suffix = uri.substring(PATH_PREFIX.length() + 1);
        } catch (Exception e) {
            log.debug("资源不存在 - {}", uri);
            response.setHttpStatus(HttpStatus.SEE_OTHER);
            response.setHeader(HeaderNameEnum.LOCATION.getName(), NOT_FOUND_PAGE);
            return false;
        }

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
            log.debug("资源不存在 - {}", uri);
            response.setHttpStatus(HttpStatus.SEE_OTHER);
            response.setHeader(HeaderNameEnum.LOCATION.getName(), NOT_FOUND_PAGE);
        } else {
            // 资源若存在，则输出到响应体
            IoUtil.copy(stream, response.getOutputStream());
        }

        return false;
    }
}
