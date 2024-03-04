package com.gitee.usl.domain;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson2.JSON;
import com.gitee.usl.api.annotation.Accessible;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.infra.structure.wrapper.BaseWrapper;
import org.smartboot.http.common.enums.HeaderNameEnum;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.common.utils.Mimetypes;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
public class HttpResponseWrapper implements BaseWrapper<HttpResponse> {

    private HttpResponse httpResponse;

    @Accessible
    public void set_content_type_by_resource(String name) {
        String mimetype = Mimetypes.getInstance().getMimetype(name);
        this.set_content_type(mimetype + StringConstant.CONTENT_TYPE_SUFFIX);
    }

    @Accessible
    public void set_content_type(String contentType) {
        httpResponse.setContentType(contentType);
    }

    @Accessible
    public void write_to_stream_by_resource(String resource, String error) {
        InputStream inputStream = ResourceUtil.getStreamSafe(resource);
        if (inputStream == null) {
            httpResponse.setHttpStatus(HttpStatus.SEE_OTHER);
            httpResponse.setHeader(HeaderNameEnum.LOCATION.getName(), error);
        }
        IoUtil.copy(inputStream, httpResponse.getOutputStream());
    }

    @Accessible
    public void write_to_string(String obj) {
        httpResponse.setContentType(HeaderValueEnum.DEFAULT_CONTENT_TYPE.getName());
        try {
            httpResponse.write(obj.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }

    @Accessible
    public void write_to_json(Object obj) {
        httpResponse.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
        try {
            httpResponse.write(JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }

    @Override
    public HttpResponse get() {
        return this.httpResponse;
    }

    @Override
    public HttpResponse set(HttpResponse value) {
        return this.httpResponse = value;
    }
}
