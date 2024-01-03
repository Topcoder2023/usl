package com.gitee.usl.grammar.asm;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLCompileException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Getter
@Setter
@Description("空脚本")
@Accessors(chain = true)
public class ES extends LS {

    @Description("异常信息")
    private USLCompileException exception;

    private ES() {
        super(null, null, null);
    }

    public static ES empty() {
        return new ES();
    }

}
