package com.gitee.usl.grammar.runtime;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.lexer.token.Token;
import lombok.Getter;
import lombok.ToString;

/**
 * @author hongda.li
 */
@Getter
@ToString
public class FunctionArgument {

    @Description("参数所在函数的Token头")
    private final Token<?> head;

    @Description("参数索引")
    private final int index;

    @Description("参数字面值")
    private final String expression;

    public FunctionArgument(final Token<?> head,
                            final int index,
                            final String name) {
        this.head = head;
        this.index = index;
        this.expression = name;
    }

}
