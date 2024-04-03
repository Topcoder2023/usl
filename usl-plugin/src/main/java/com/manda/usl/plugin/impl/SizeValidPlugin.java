package com.manda.usl.plugin.impl;

import cn.hutool.core.util.ObjectUtil;
import com.manda.usl.kernel.common.USLException;
import com.manda.usl.kernel.entity.FunctionSession;
import com.manda.usl.plugin.annotation.Size;
import com.manda.usl.plugin.entity.Location;

/**
 * 长度校验插件
 * 根据Size注解对函数参数进行长度校验
 *
 * @author jiahao.song
 */
public class SizeValidPlugin extends AbstractValidPlugin<Size> {

    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Size annotation, Object actual) {
        // Size注解预期的长度
        int exactSize = annotation.value();
        int minSize = annotation.min();
        int maxSize = annotation.max();

        // 参数实际值校验
        int actualSize = ObjectUtil.length(actual);

        String message = annotation.message();

        if (exactSize != -1 && actualSize != exactSize) {
            // 精确长度
            throwException(message, location, actualSize, String.valueOf(exactSize));
        } else if (exactSize == -1) {
            // 非精确长度
            if (minSize != -1 && maxSize == -1) {
                // 有最小值无最大值
                if (actualSize < minSize) {
                    throwException(message, location, actualSize, minSize + " - ");
                }
            } else if (minSize == -1 && maxSize != -1) {
                // 有最大值无最小值
                if (actualSize > maxSize) {
                    throwException(message, location, actualSize, " - " + maxSize);
                }
            } else {
                // 有最小值和最大值
                if (actualSize < minSize || actualSize > maxSize) {
                    throwException(message, location, actualSize,
                            minSize + " - " + maxSize);
                }
            }
        }
    }

    private void throwException(String message, Location location, int actualSize, String expect) {
        // 替换预置变量
        String replace = message.replace("{name}", location.getName())
                .replace("{index}", String.valueOf(location.getIndex()))
                .replace("{value}", String.valueOf(actualSize))
                .replace("{expect}", expect);

        // 抛出校验异常
        throw new USLException(replace, location, actualSize);
    }
}
