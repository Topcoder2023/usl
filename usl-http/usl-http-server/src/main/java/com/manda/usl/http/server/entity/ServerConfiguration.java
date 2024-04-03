package com.manda.usl.http.server.entity;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.text.StrPool;
import lombok.Data;

/**
 * @author hongda.li
 */
@Data
public class ServerConfiguration {

    private String name;

    private String host = NetUtil.getLocalhostStr();

    private Integer port = NetUtil.getUsableLocalPort();

    private String resource = StrPool.SLASH;

    private Integer threadNum = 8;

    private Integer bufferSize = 1 << 10;

    private Integer idleTimeout = 60 * 1000;

    private Integer maxRequestSize = Integer.MAX_VALUE;
}
