package com.gitee.usl.kernel.domain;

import cn.hutool.core.io.resource.ResourceUtil;
import com.gitee.usl.infra.exception.UslNotFoundException;

/**
 * 资源参数
 * 用以快速加载类路径下的资源文件作为脚本
 *
 * @author hongda.li
 */
public class UslResourceParam extends UslParam {
    private static final String NOT_FOUND = "Resource not found in classpath [{}]";
    private final String resource;

    public UslResourceParam(String resource) {
        this.resource = resource;
        init();
    }

    private void init() {
        try {
            String utf8Str = ResourceUtil.readUtf8Str(this.resource);
            this.setContent(utf8Str);
        } catch (Exception e) {
            throw new UslNotFoundException(NOT_FOUND, this.resource);
        }
    }

    public String getResource() {
        return resource;
    }
}
