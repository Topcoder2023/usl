package com.gitee.usl.api;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.type.USLFunction;

import java.util.List;

/**
 * @author hongda.li
 */
@Description("可重载函数接口")
public interface Overloaded<T extends USLFunction> extends Definable {

    @Description("获取所有重载实现")
    List<T> allOverloadImpl();

    @Description("添加重载实现")
    void addOverloadImpl(Overloaded<?> impl);

}