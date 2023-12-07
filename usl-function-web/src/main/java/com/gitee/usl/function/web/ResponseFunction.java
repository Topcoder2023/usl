package com.gitee.usl.function.web;

import cn.hutool.http.ContentType;
import cn.hutool.http.server.HttpServerResponse;
import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.api.annotation.Func;

/**
 * @author hongda.li
 */
@Func("response.")
public class ResponseFunction {

    @Func("write.string")
    public HttpServerResponse write(HttpServerResponse response, String obj) {
        response.write(obj, ContentType.TEXT_PLAIN.getValue());
        return response;
    }

    @Func("write.json")
    public HttpServerResponse write(HttpServerResponse response, Object obj) {
        response.write(JsonKit.toJson(obj), ContentType.JSON.getValue());
        return response;
    }
}
