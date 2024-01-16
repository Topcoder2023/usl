package com.gitee.usl.kernel.domain;

import cn.hutool.core.io.resource.ResourceUtil;
import com.gitee.usl.api.annotation.Description;
import lombok.Getter;
import lombok.ToString;

/**
 * @author hongda.li
 */
@Getter
@ToString
@Description("资源参数，用以快速加载类路径下的资源文件作为脚本")
public class ResourceParam extends Param {

    @Description("资源路径")
    private final String resource;

    public ResourceParam(String resource) {
        super(ResourceUtil.readUtf8Str(resource));
        this.resource = resource;
    }

}
