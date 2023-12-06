package com.gitee.usl.function.base;

import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.api.annotation.Func;

import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@Func
public class JsonFunction {

    @Func("json.toJson")
    public String toJson(Object obj) {
        return JsonKit.toJson(obj);
    }

    @Func("json.toList")
    @SuppressWarnings("rawtypes")
    public List<Map> toList(String json) {
        return JsonKit.toList(Map.class, json);
    }

    @Func("json.toMap")
    @SuppressWarnings("rawtypes")
    public Map toMap(String json) {
        return JsonKit.toBean(Map.class, json);
    }
}
