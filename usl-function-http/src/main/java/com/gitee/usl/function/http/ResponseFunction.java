package com.gitee.usl.function.http;

import com.alibaba.fastjson2.JSON;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.USLException;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ResponseFunction {

    @Function("http_resp_string")
    public void write(HttpResponse response, String obj) {
        response.setContentType(HeaderValueEnum.DEFAULT_CONTENT_TYPE.getName());
        try {
            response.write(obj.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }

    @Function("http_resp_json")
    public void write(HttpResponse response, Object obj) {
        response.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
        try {
            response.write(JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLException(e);
        }
    }
}
