package com.manda.usl.data.sqlite.entity;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import java.util.List;

/**
 * @author hongda.li
 */
@Data
public class DatabaseConfig {

    private String url;

    private String username;

    private String password;

    private List<String> mapper;

    private Boolean testWhileIdle = Boolean.FALSE;

    private Integer minIdle = DruidDataSource.DEFAULT_MIN_IDLE;

    private Long maxWait = (long) DruidDataSource.DEFAULT_MAX_WAIT;

    private Integer maxActive = DruidDataSource.DEFAULT_MAX_ACTIVE_SIZE;

    private Integer initialSize = DruidDataSource.DEFAULT_INITIAL_SIZE;

    private Boolean testOnBorrow = DruidDataSource.DEFAULT_TEST_ON_BORROW;

    private Boolean testOnReturn = DruidDataSource.DEFAULT_TEST_ON_RETURN;
}
