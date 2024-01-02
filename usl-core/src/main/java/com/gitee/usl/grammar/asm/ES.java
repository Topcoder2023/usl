package com.gitee.usl.grammar.asm;

import com.gitee.usl.api.annotation.Description;

/**
 * @author hongda.li
 */
@Description("空脚本")
public class ES extends LS {
    @Description("空脚本实现")
    private static final ES INSTANCE = new ES();

    private ES() {
        super(null, null, null);
    }

    public static Script empty() {
        return INSTANCE;
    }

}
