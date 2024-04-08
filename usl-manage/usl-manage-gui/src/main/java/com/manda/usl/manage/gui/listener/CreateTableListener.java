package com.manda.usl.manage.gui.listener;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.db.Db;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.event.Event;
import com.manda.usl.kernel.bean.event.EventListener;
import com.manda.usl.kernel.bean.event.ReadyEvent;
import com.manda.usl.manage.gui.service.FileService;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Slf4j
@Singleton
public class CreateTableListener implements EventListener<ReadyEvent> {

    @Override
    public void listen(Event event) {
        try {
            Db.use(BeanContext.findFirst(DataSource.class)).execute(ResourceUtil.readUtf8Str("sql/File.sql"));
        } catch (SQLException e) {
            log.error("Error in creating `FILE` table", e);
        }

        BeanContext.findFirst(FileService.class).loadFileView();
    }
}
