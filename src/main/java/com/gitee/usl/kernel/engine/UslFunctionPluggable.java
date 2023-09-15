package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.exception.UslExecuteException;
import com.gitee.usl.kernel.plugin.*;

import java.util.List;
import java.util.function.Consumer;

/**
 * 可插件化的接口
 *
 * @author hongda.li
 */
public interface UslFunctionPluggable {
    Object doCommand();

    List<UslPlugin> getPluginList();

    default Object withPlugin() {
        makePlugin(UslBeginPlugin.class, UslBeginPlugin::onBegin);

        try {
            Object result = doCommand();

            makePlugin(UslSuccessPlugin.class, UslSuccessPlugin::onSuccess);

            return result;
        } catch (Exception e) {

            makePlugin(UslFailurePlugin.class, UslFailurePlugin::onFailure);

            throw new UslExecuteException(e);
        } finally {

            makePlugin(UslFinallyPlugin.class, UslFinallyPlugin::onFinally);
        }
    }

    private <T> void makePlugin(Class<T> pluginType, Consumer<T> consumer) {
        getPluginList().stream()
                .filter(plugin -> pluginType.isAssignableFrom(plugin.getClass()))
                .map(pluginType::cast)
                .forEach(consumer);
    }
}
