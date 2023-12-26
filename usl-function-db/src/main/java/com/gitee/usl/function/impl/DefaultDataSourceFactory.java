package com.gitee.usl.function.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.pooled.PooledDSFactory;
import cn.hutool.setting.Setting;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.function.api.DataSourceFactory;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;

import java.util.Optional;

/**
 * @author hongda.li
 */
@AutoService(Initializer.class)
public class DefaultDataSourceFactory implements Initializer {
    private static final String DATABASE_PREFIX = "database.";

    @Override
    public void doInit(Configuration configuration) {
        DataSourceFactory searched = Optional.ofNullable(ServiceSearcher.searchFirst(DataSourceFactory.class))
                .orElse(config -> {
                    Setting setting = new Setting();
                    config.getCustomConfig()
                            .forEach((key, value) -> {
                                if (CharSequenceUtil.startWith(key, DATABASE_PREFIX)) {
                                    setting.set(CharSequenceUtil.removePrefix(key, DATABASE_PREFIX), String.valueOf(value));
                                }
                            });
                    return new PooledDSFactory(setting);
                });

        DSFactory.setCurrentDSFactory(searched.createFactory(configuration));
    }
}
