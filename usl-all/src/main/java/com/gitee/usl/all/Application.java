package com.gitee.usl.all;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.enums.InteractiveMode;

/**
 * @author hongda.li
 */
public class Application {
    public static void main(String[] args) {
        if (ArrayUtil.isEmpty(args)) {
            new USLRunner().start();
        } else {
            new USLRunner().start(InteractiveMode.of(args[0]));
        }
    }
}
