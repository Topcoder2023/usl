package com.gitee.usl.cli;

import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.enums.InteractiveMode;

/**
 * @author hongda.li
 */
public class CliInteractiveTest {
    public static void main(String[] args) {
        new USLRunner().start(InteractiveMode.CLI);
    }
}
