package com.manda.usl.http.client.entity;

import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Data
public class ClientConfiguration {

    private Integer maxIdle = 5;

    private Integer keepAlive = 5 * 60;

    private Long readTimeout = 60L;

    private Long writeTimeout = 60L;

    private Long connectTimeout = 30L;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private List<String> clients;
}
