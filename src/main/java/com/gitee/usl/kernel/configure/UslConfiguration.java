package com.gitee.usl.kernel.configure;

import com.gitee.usl.api.Interaction;
import com.gitee.usl.app.interaction.EmptyInteraction;
import com.gitee.usl.infra.utils.ServiceSearcher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * USL 配置类
 * USL 配置类由不同功能模块的配置类共同组成
 * 配置类通常不会直接暴露其底层实现
 * 每个具体的配置类持有当前功能模块的配置参数与管理者实例
 * 例如，QueueConfiguration 持有 CompileQueueManager 实例
 * CacheConfiguration 持有 UslCache 实例
 * 因此，可以通过 USL 配置类获取任意功能模块的管理者
 * 并通过功能模块的管理者进行模块之间的调度
 * 同时，为了更好的扩展其它未知参数，USL 预留了 customConfiguration 自定义的 Map 类型
 *
 * @author hongda.li
 */
public final class UslConfiguration {
    private Interaction interaction = EmptyInteraction.getInstance();
    /**
     * 缓存配置类
     */
    private final CacheConfiguration cacheConfiguration = new CacheConfiguration(this);

    /**
     * 编译队列配置类
     */
    private final QueueConfiguration queueConfiguration = new QueueConfiguration(this);

    /**
     * 自定义扩展配置类
     */
    private final Map<String, Object> customConfiguration = new ConcurrentHashMap<>();

    /**
     * 脚本引擎配置类
     */
    private final EngineConfiguration engineConfiguration = new EngineConfiguration(this);

    /**
     * 线程池配置类
     */
    private final ThreadPoolConfiguration threadPoolConfiguration = new ThreadPoolConfiguration(this);

    /**
     * 网络服务配置类
     */
    private final WebServerConfiguration webServerConfiguration = new WebServerConfiguration(this);

    public UslConfiguration changeInteraction(Class<? extends Interaction> interaction) {
        this.interaction = ServiceSearcher.searchFirst(interaction);
        return this;
    }

    public Interaction interaction() {
        return this.interaction;
    }

    public CacheConfiguration configCache() {
        return cacheConfiguration;
    }

    public QueueConfiguration configQueue() {
        return queueConfiguration;
    }

    public Map<String, Object> configCustom() {
        return customConfiguration;
    }

    public EngineConfiguration configEngine() {
        return engineConfiguration;
    }

    public ThreadPoolConfiguration configThreadPool() {
        return threadPoolConfiguration;
    }

    public WebServerConfiguration configWebServer() {
        return webServerConfiguration;
    }
}
