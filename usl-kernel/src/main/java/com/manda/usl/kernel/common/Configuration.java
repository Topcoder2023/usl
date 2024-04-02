package com.manda.usl.kernel.common;

import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.Optional;

/**
 * 全局配置
 *
 * @author hongda.li
 */
public class Configuration extends JSONObject {
    /**
     * 解析配置文件
     *
     * @param configName 配置文件名称
     * @return 解析结果
     */
    public static Configuration parse(String configName) {
        try {
            Configuration configuration = JSON.parseObject(ResourceUtil.readUtf8Str(configName), Configuration.class);
            return Optional.ofNullable(configuration).orElse(new Configuration());
        } catch (Exception ignored) {
            return new Configuration();
        }
    }

    /**
     * 读取配置文件的指定项
     *
     * @param path JSON 表达式
     * @param type 解析类型
     * @param <C>  解析泛型
     * @return 解析结果
     */
    public <C> C read(String path, Class<C> type) {
        Object result = this.getByPath(path);
        if (result instanceof JSONObject jsonObject) {
            return jsonObject.to(type);
        } else {
            return type.cast(result);
        }
    }
}
