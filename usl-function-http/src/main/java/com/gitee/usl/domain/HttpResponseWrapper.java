package com.gitee.usl.domain;

import com.alibaba.fastjson2.JSON;
import com.gitee.usl.api.annotation.Accessible;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.infra.structure.wrapper.BaseWrapper;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
public class HttpResponseWrapper implements BaseWrapper<HttpResponse> {
    private HttpResponse httpResponse;

    @Override
    public HttpResponse get() {
        return this.httpResponse;
    }

    @Override
    public HttpResponse set(HttpResponse value) {
        return this.httpResponse = value;
    }

    @Accessible
    public void write_to_string(HttpResponse response, String obj) {
        response.setContentType(HeaderValueEnum.DEFAULT_CONTENT_TYPE.getName());
        try {
            response.write(obj.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }

    @Accessible
    public void write_to_json(HttpResponse response, Object obj) {
        response.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
        try {
            response.write(JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }
}
