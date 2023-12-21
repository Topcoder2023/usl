package com.gitee.usl.function.api;

import cn.hutool.db.ds.DSFactory;
import com.gitee.usl.kernel.configure.Configuration;

/**
 * @author hongda.li
 */
public interface DataSourceFactory {
    /**
     * 创建数据源工厂
     *
     * @param configuration 配置类
     * @return 数据源工厂
     */
    DSFactory createFactory(Configuration configuration);
}
