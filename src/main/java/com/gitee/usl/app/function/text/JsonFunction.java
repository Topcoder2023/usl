package com.gitee.usl.app.function.text;

import com.alibaba.fastjson2.JSON;
import com.gitee.usl.api.annotation.Func;

import java.util.List;

/**
 * @author hongda.li
 */
@Func
public class JsonFunction {

    @Func("json.to")
    public String toJson(Object any) {
        return JSON.toJSONString(any);
    }

    @Func({"json.from", "json.from.obj"})
    public <T> T fromJson(String json, Class<T> type) {
        return JSON.parseObject(json, type);
    }

    @Func({"json.from.list", "json.from.array"})
    public <T> List<T> fromJsonList(String json, Class<T> type) {
        return JSON.parseArray(json, type);
    }
}
