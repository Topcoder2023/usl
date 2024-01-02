package com.gitee.usl.grammar.asm;

import com.gitee.usl.api.annotation.Description;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.parser.VariableMeta;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@Getter
@ToString
public class LS extends BS {

    @Description("常量结果")
    private final Object result;

    public LS(final AviatorEvaluatorInstance instance,
              final Object result,
              final List<VariableMeta> vars) {
        super(instance, vars, null);
        this.result = result;
    }

    @Override
    public Object defaultImpl(final Map<String, Object> env) {
        return this.result;
    }

}
