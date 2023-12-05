package com.gitee.usl.plugin.impl.sensitive;

import cn.hutool.core.util.DesensitizedUtil;
import com.gitee.usl.plugin.api.SensitizedStrategy;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author hongda.li
 */
public class DefaultSensitizedStrategy implements SensitizedStrategy {
    @Override
    public boolean condition(SensitiveContext context) {
        String expect = context.getType().name();
        return Arrays.stream(DesensitizedUtil.DesensitizedType.values())
                .map(Enum::name)
                .anyMatch(accept -> Objects.equals(expect, accept));
    }

    @Override
    public void accept(SensitiveContext context) {
        DesensitizedUtil.DesensitizedType type = DesensitizedUtil.DesensitizedType.valueOf(context.getType().name());
        String result = DesensitizedUtil.desensitized(String.valueOf(context.context()), type);
        context.setResult(result);
    }
}
