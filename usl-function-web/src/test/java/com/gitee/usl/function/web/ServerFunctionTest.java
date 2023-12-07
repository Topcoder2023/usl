package com.gitee.usl.function.web;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class ServerFunctionTest {
    static USLRunner runner;

    public static void main(String[] args) {
        runner = new USLRunner();
        runner.start();

        String str = "fn handler(req, resp){\n" +
                "response.write.string(resp, 'hello world');" +
                "}\n" +
                "httpServer = server(10010);\n" +
                "server.route(httpServer, '/hello', handler);\n" +
                "server.start(httpServer);";

        runner.run(new Param().setScript(str));
    }

    @Test
    void serverTest() {
        assertDoesNotThrow(() -> runner.run(new Param().setScript("server(10001)")));
    }
}