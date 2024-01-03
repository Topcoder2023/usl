package com.gitee.usl.kernel.domain;

import cn.hutool.core.io.resource.ResourceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLNotFoundException;
import lombok.Getter;
import lombok.ToString;

/**
 * @author hongda.li
 */
@Getter
@ToString
@Description("资源参数，用以快速加载类路径下的资源文件作为脚本")
public class ResourceParam extends Param {

    @Description("错误消息")
    private static final String NOT_FOUND = "Resource not found in classpath [{}]";

    @Description("资源路径")
    private final String resource;

    public ResourceParam(String resource) {
        super();
        this.resource = resource;
        init();
    }

    private void init() {
        try {
            this.setScript(ResourceUtil.readUtf8Str(this.resource));
        } catch (Exception e) {
            throw new USLNotFoundException(NOT_FOUND, this.resource);
        }
    }

}
