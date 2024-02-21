package com.gitee.usl.infra.enums;

import com.gitee.usl.api.annotation.Description;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Getter
public enum EngineName {

    @Description("JavaScript")
    JS("JavaScript"),

    @Description("AviatorScript")
    AS("AviatorScript"),

    @Description("Groovy")
    GROOVY("Groovy"),

    @Description("Python")
    PYTHON("python");

    private final String name;

    EngineName(String name) {
        this.name = name;
    }

}
