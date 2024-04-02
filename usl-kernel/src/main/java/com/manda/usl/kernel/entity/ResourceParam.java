package com.manda.usl.kernel.entity;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.Getter;
import lombok.ToString;

/**
 * @author hongda.li
 */
@Getter
@ToString
public class ResourceParam extends Param {

    private final String resource;

    public ResourceParam(String resource) {
        super(ResourceUtil.readUtf8Str(resource));
        this.resource = resource;
    }

}
