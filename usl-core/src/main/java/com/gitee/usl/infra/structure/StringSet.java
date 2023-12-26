package com.gitee.usl.infra.structure;

import com.gitee.usl.infra.constant.NumberConstant;

import java.util.LinkedHashSet;

/**
 * @author hongda.li
 */
public class StringSet extends LinkedHashSet<String> {

    public StringSet() {
        super(NumberConstant.COMMON_SIZE);
    }
}
