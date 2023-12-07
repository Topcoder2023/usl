package com.gitee.usl.function.web;

import cn.hutool.http.ContentType;
import cn.hutool.http.server.HttpServerResponse;
import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ResponseFunction {

    @Function("response_write_string")
    public HttpServerResponse write(HttpServerResponse response, String obj) {
        response.write(obj, ContentType.TEXT_PLAIN.getValue());
        return response;
    }

    @Function("response_write_json")
    public HttpServerResponse write(HttpServerResponse response, Object obj) {
        response.write(JsonKit.toJson(obj), ContentType.JSON.getValue());
        return response;
    }
}
