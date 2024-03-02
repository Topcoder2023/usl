package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.Definable;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.kernel.plugin.LoggerPlugin;
import org.slf4j.Logger;

/**
 * @author hongda.li
 */
public class LoggerPluginEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhancePluggable(FunctionPluggable fp) {
        boolean skip;

        if (fp instanceof Definable definable) {
            boolean disableDebug = !Boolean.TRUE.equals(definable.definition()
                    .getRunner()
                    .getConfiguration()
                    .getEnableDebug());

            Class<?> targeted = definable.definition()
                    .getMethodMeta()
                    .targetType();

            boolean hasLogger = ArrayUtil.isNotEmpty(ReflectUtil.getFields(targeted, field -> Logger.class.equals(field.getType())));
            skip = disableDebug || hasLogger;
        } else {
            skip = false;
        }

        // 如果函数所在的类中，已存在字段类型为 Slf4j 的日志门面，则跳过安装日志插件
        // 如果未开启调试模式，则跳过安装日志插件
        if (skip) {
            return;
        }

        fp.plugins().install(Singleton.get(LoggerPlugin.class));
    }
}
