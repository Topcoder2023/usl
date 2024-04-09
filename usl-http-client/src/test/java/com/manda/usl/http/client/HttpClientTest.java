package com.manda.usl.http.client;

import cn.hutool.core.io.IoUtil;
import cn.zhxu.okhttps.HttpResult;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.bean.BeanContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class HttpClientTest {

    @Test
    void test() {
        USLRunner.run();

        HttpClient client = BeanContext.findFirst(HttpClient.class);
        System.out.println(IoUtil.readUtf8(client.getHttpClient()
                .sync("https://www.baidu.com")
                .get()
                .getBody()
                .toByteStream()));
    }
}