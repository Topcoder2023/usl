package com.gitee.usl.app.cli;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Order;
import com.google.auto.service.AutoService;

/**
 * Read-Eval-Print Loop
 * 接收来自本地终端的请求
 *
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE)
@AutoService(CliInteractive.class)
public class CliInteractiveImpl implements CliInteractive {
    @Override
    public void open(USLRunner runner) {

    }
}
