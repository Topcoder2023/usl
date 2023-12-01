package com.gitee.usl.all;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.enums.InteractiveMode;
import com.gitee.usl.kernel.configure.Configuration;

/**
 * @author hongda.li
 */
public class Application {
    public static void main(String[] args) {
        final USLRunner runner = new USLRunner(Application.globalConfig());

        if (ArrayUtil.isEmpty(args)) {
            runner.start();
        } else {
            runner.start(InteractiveMode.of(args[0]));
        }
    }

    public static Configuration globalConfig() {
        return USLRunner.defaultConfiguration();
    }
}
