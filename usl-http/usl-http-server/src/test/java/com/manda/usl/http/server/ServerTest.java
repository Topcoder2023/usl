package com.manda.usl.http.server;

import cn.hutool.core.date.DateTime;
import com.manda.usl.http.server.anontation.Mapping;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.annotation.Param;
import com.manda.usl.kernel.annotation.Singleton;

/**
 * @author hongda.li
 */
@Singleton
@Mapping("/test")
class ServerTest {

    @Mapping("/date/{name}")
    public String newDate(@Param("name") String name) {
        return new DateTime() + name;
    }

    public static void main(String[] args) {
        USLRunner.run();
    }
}