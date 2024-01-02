package com.gitee.usl.kernel.domain;

import com.gitee.usl.api.annotation.Description;
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

    @Description("是否缓存表达式")
    private boolean cached;

    @Description("脚本内容")
    private String script;

    @Description("上下文变量")
    private StringMap<Object> context;

    public Param() {
        this.cached = true;
        this.context = new StringMap<>();
    }

    public Param(String script) {
        this.script = script;
        this.cached = true;
        this.context = new StringMap<>();
    }

    public Param addContext(String name, Object value) {
        this.context.put(name, value);
        return this;
    }

    public Param addContext(Map<String, Object> other) {
        if (other != null) {
            this.context.putAll(other);
        }
        return this;
    }

    public Param setContext(Map<String, Object> map) {
        this.context = new StringMap<>(map);
        return this;
    }

}
