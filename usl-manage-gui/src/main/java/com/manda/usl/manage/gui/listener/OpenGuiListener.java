package com.manda.usl.manage.gui.listener;

import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.core.util.URLUtil;
import com.manda.usl.http.server.entity.ServerConfiguration;
import com.manda.usl.http.server.mapping.StaticHandler;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.bean.event.Event;
import com.manda.usl.kernel.bean.event.EventListener;
import com.manda.usl.kernel.bean.event.ReadyEvent;

/**
 * @author hongda.li
 */
@Singleton
public class OpenGuiListener implements EventListener<ReadyEvent> {
    @Override
    public void listen(Event event) {
        ServerConfiguration configuration = USLRunner.getConfiguration().read("http.server", ServerConfiguration.class);
        String path = URLUtil.normalize(configuration.getHost()
                + ":"
                + configuration.getPort()
                + StaticHandler.PREFIX + "/index.html");
        DesktopUtil.browse(path);
    }
}
