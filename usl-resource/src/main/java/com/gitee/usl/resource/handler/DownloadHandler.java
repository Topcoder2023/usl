package com.gitee.usl.resource.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.resource.api.WebHandler;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.common.enums.HeaderNameEnum;
import org.smartboot.http.common.utils.Mimetypes;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
@AutoService(WebHandler.class)
public class DownloadHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/download";
    private static final String DOWNLOAD_URL = "downloadUrl";
    private static final String DOWNLOAD_HEADER = "content-disposition";
    private static final String DOWNLOAD_HEADER_VALUE = "attachment; filename=";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        String param = getParam(DOWNLOAD_URL, String.class);
        boolean isNotHttp = !HttpUtil.isHttp(param) && !HttpUtil.isHttps(param);
        if (CharSequenceUtil.isBlank(param) || isNotHttp) {
            this.redirect(StaticRequestHandler.NOT_FOUND_PAGE);
            return;
        }

        String fileName = FileUtil.getName(param);

        response.setHeader(DOWNLOAD_HEADER, DOWNLOAD_HEADER_VALUE + fileName);
        String contentType = Mimetypes.getInstance().getMimetype(fileName);
        response.setHeader(HeaderNameEnum.CONTENT_TYPE.getName(), contentType + StringConstant.CONTENT_TYPE_SUFFIX);

        HttpUtil.download(param, response.getOutputStream(), false, new StreamProgress() {
            @Override
            public void start() {
                logger.debug("开始下载文件 - {}", param);
            }

            @Override
            public void progress(long total, long progressSize) {
                // do nothing
            }

            @Override
            public void finish() {
                logger.debug("文件下载成功");
            }
        });
    }
}
