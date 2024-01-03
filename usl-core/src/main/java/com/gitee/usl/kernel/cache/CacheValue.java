package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.grammar.asm.Script;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Description("缓存值")
@Accessors(chain = true)
public class CacheValue {

    @Description("表达式编译值")
    private Script script;

    @Description("变量初始值")
    private StringMap<Object> initEnv;

    @Description("构造一个缓存值")
    public static CacheValue of(StringMap<Object> initEnv, Script expression) {
        return new CacheValue().setInitEnv(initEnv).setScript(expression);
    }

}
