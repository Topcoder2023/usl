package com.gitee.usl.domain;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.infra.structure.StringMap;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
@Description("USL 参数结构")
public class Param {

    @Description("脚本内容")
    private final String script;

    @Description("是否缓存表达式")
    private final boolean cached;

    @Description("上下文变量")
    private final StringMap<Object> context = new StringMap<>();

    @Description("表达式编译值")
    private Script compiled;

    public Param(String script) {
        this(true, script);
    }

    public Param self() {
        return new Param(this.cached, this.script);
    }

    public Param(boolean cached, String script) {
        this.script = script;
        this.cached = cached;
    }

    public Param addContext(String name, Object value) {
        this.context.put(name, value);
        return this;
    }

    public Param addContext(Map<String, Object> other) {
        this.context.putAll(other);
        return this;
    }

}
