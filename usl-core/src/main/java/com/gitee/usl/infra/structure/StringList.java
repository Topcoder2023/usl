package com.gitee.usl.infra.structure;

import com.gitee.usl.infra.constant.NumberConstant;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author hongda.li
 */
public class StringList extends ArrayList<String> {
    private static final StringList EMPTY = new StringList(NumberConstant.ZERO);
    private static final long serialVersionUID = -3906350419629179610L;

    public StringList() {
        super();
    }

    public StringList(int size) {
        super(size);
    }

    public StringList(Collection<String> collection) {
        super(collection);
    }

    public static StringList empty() {
        return EMPTY;
    }
}
