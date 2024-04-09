package com.manda.usl.data.sql.mapper;

import cn.hutool.db.Db;
import com.manda.usl.data.sql.entity.Metadata;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author hongda.li
 */
@Data
public class InterceptorContext {

    private String sql;

    private Db database;

    private Object[] args;

    private Object result;

    private Method method;

    private Metadata metadata;
}
