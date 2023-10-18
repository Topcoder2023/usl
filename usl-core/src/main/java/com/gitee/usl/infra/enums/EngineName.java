package com.gitee.usl.infra.enums;

/**
 * @author hongda.li
 */
public enum EngineName {
    /**
     * JavaScript
     */
    JS("JavaScript"),

    /**
     * AviatorScript
     */
    AS("AviatorScript"),
    /**
     * Groovy
     */
    GROOVY("Groovy"),
    /**
     * Python
     */
    PYTHON("python");

    private final String name;

    EngineName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
