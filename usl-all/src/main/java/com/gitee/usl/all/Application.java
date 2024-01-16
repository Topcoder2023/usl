package com.gitee.usl.all;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.Runner;
import com.gitee.usl.infra.enums.InteractiveMode;
import com.gitee.usl.kernel.configure.Configuration;

/**
 * @author hongda.li
 */
public class Application {
    public static void main(String[] args) {
        final Runner runner = new Runner(Application.globalConfig());

        if (ArrayUtil.isEmpty(args)) {
            runner.start();
        } else {
            runner.start(InteractiveMode.of(args[0]));
        }
    }

    public static Configuration globalConfig() {
        return Runner.defaultConfiguration();
    }
}
