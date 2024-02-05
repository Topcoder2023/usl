package com.gitee.usl.plugin.domain;

import com.gitee.usl.grammar.lexer.token.Token;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@ToString
@Accessors(chain = true)
public class Location {
    /**
     * 函数名称
     */
    private String name;

    /**
     * 参数索引（初始位从 1 开始）
     */
    private Integer index;

    /**
     * 参数所在行号
     */
    private Integer lineNo;

    /**
     * 参数所在起始位置
     */
    private Integer lineStartIndex;

    /**
     * 参数所在终止位置
     */
    private Integer lineEndIndex;

    /**
     * 根据 Token 信息构造参数位置实例
     *
     * @param from Token 信息
     * @return 参数位置信息
     */
    public static Location from(Token<?> from) {
        if (from == null) {
            return new Location();
        } else {
            return new Location()
                    .setLineNo(from.getLineNo())
                    .setLineStartIndex(from.getStartIndex())
                    .setLineEndIndex(from.getEndIndex());
        }
    }
}
