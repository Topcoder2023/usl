package com.gitee.usl.api.service;

import cn.hutool.core.convert.Convert;
import com.gitee.usl.api.EnvLoader;
import com.gitee.usl.infra.utils.SpiServiceUtil;

import java.util.List;
import java.util.Optional;

/**
 * USL 环境变量加载器
 * 该类使用单例模式实现
 *
 * @author hongda.li
 */
public class UslEnvLoader implements EnvLoader {
    /**
     * 环境变量加载器实例集合
     * 该集合在本类首次加载时被初始化
     */
    private static final List<EnvLoader> LOADER_LIST;

    /**
     * USL环境变量加载器全局唯一实例
     */
    private static final UslEnvLoader LOADER = new UslEnvLoader();

    private UslEnvLoader() {
    }

    static {
        // 根据SPI机制加载所有可用的环境变量加载器并排序
        LOADER_LIST = SpiServiceUtil.loadSortedService(EnvLoader.class);
    }

    /**
     * 获取全局唯一的 USL 环境变量加载器实例
     *
     * @return USL 环境变量加载器实例
     */
    public static UslEnvLoader getInstance() {
        return LOADER;
    }

    @Override
    public String fetch(String name) {
        // 遍历所有环境变量加载器并获取首个加载成功的值
        // 若所有环境变量加载器均无法加载此值，则返回空
        for (EnvLoader loader : LOADER_LIST) {
            String fetched;
            if ((fetched = loader.fetch(name)) != null) {
                return fetched;
            }
        }

        return null;
    }

    //===============================获取环境变量并当变量不存在时给定默认值==============================================

    public Integer getAsInt(String name, Integer defaultValue) {
        return Convert.toInt(this.fetch(name), defaultValue);
    }

    public Long getAsLong(String name, Long defaultValue) {
        return Convert.toLong(this.fetch(name), defaultValue);
    }

    public Boolean getAsBool(String name, Boolean defaultValue) {
        return Convert.toBool(this.fetch(name), defaultValue);
    }

    public Double getAsDouble(String name, Double defaultDouble) {
        return Convert.toDouble(this.fetch(name), defaultDouble);
    }

    public Float getAsFloat(String name, Float defaultFloat) {
        return Convert.toFloat(this.fetch(name), defaultFloat);
    }

    public Byte getAsByte(String name, Byte defaultByte) {
        return Convert.toByte(this.fetch(name), defaultByte);
    }

    public String getAsStr(String name, String defaultValue) {
        return Optional.ofNullable(this.fetch(name)).orElse(defaultValue);
    }
}
