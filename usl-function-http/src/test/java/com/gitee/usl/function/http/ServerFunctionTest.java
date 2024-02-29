package com.gitee.usl.function.http;

import com.gitee.usl.USLRunner;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.ResourceParam;
import com.gitee.usl.domain.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class ServerFunctionTest {
    static USLRunner runner = new USLRunner();

    public static void main(String[] args) {
        runner.run(new ResourceParam("main.js"));
    }
}