package com.gitee.usl.infra.constant;

/**
 * @author hongda.li
 */
public final class ModuleConstant {
    private ModuleConstant() {
    }

    public static final String DEFAULT = "USL-Default";

    public static final String USL_CORE = "USL-Core";

    public static final String USL_RESOURCE = "USL-Resource";

    public static Boolean isFrom(String belong) {
        return USL_CORE.equals(belong) || USL_RESOURCE.equals(belong);
    }
}
