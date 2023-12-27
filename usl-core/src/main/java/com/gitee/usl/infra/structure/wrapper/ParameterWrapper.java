package com.gitee.usl.infra.structure.wrapper;

import com.gitee.usl.api.annotation.Description;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Parameter;

/**
 * @author hongda.li
 */
@ToString
@Description("参数包装器")
public class ParameterWrapper implements BaseWrapper<Parameter> {

    @Getter
    @Setter
    @Description("当前参数的索引")
    private int index;

    @Description("当前参数的实例")
    private Parameter parameter;

    @Override
    public Parameter get() {
        return parameter;
    }

    @Override
    public Parameter set(Parameter value) {
        this.parameter = value;
        return parameter;
    }

}
