package com.gitee.usl.function.domain;

import cn.hutool.db.Db;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public record Database(Db proxy, String path) {
    @Override
    public String toString() {
        return new StringJoiner(", ", Database.class.getSimpleName() + "[", "]")
                .add("proxy=" + proxy)
                .add("path='" + path + "'")
                .toString();
    }
}
