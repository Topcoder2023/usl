package com.gitee.usl.grammar.parser.ir;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Label {

    public String name;

    public int i;

    public Label(final int i) {
        this.i = i;
        this.name = "L" + i;
    }

}
