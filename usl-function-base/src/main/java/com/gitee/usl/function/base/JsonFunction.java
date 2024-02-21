package com.gitee.usl.function.base;

import cn.hutool.json.JSONUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@SuppressWarnings("unused")
@FunctionGroup
public class JsonFunction {

    @Function("json_toJson")
    public String toJson(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }

    @Function("json_toList")
    @SuppressWarnings("rawtypes")
    public List<Map> toList(String json) {
        return JSONUtil.toList(json, Map.class);
    }

    @Function("json_toMap")
    @SuppressWarnings("rawtypes")
    public Map toMap(String json) {
        return JSONUtil.toBean(json, Map.class);
    }

}
