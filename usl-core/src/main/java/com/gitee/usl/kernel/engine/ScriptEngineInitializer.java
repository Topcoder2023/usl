package com.gitee.usl.kernel.engine;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.infra.utils.MethodInvokerOnMissing;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@Description({"USL脚本引擎",
        "SHA256 —— 唯一缓存键生成",
        "SpinLock —— 异步加载编译结果",
        "W-TinyLFU —— 缓存淘汰算法",
        "Token-Bucket —— 令牌桶限流组件",
        "Timing-Wheel —— 时间轮任务调度",
        "Count–Min Sketch —— 编译缓存计数算法",
        "Non-blocking Synchronization —— 无锁化编程"})
@Getter
@AutoService(Initializer.class)
public final class ScriptEngineInitializer implements Initializer {

    @Description("脚本引擎实例")
    private ScriptEngine instance;

    @Override
    public void doInit(Configuration configuration) {
        EngineConfig configEngine = configuration.getEngineConfig();

        this.instance = new ScriptEngine();
        this.instance.setFunctionLoader(name -> configEngine.getFunctionHolder().search(name));
        this.instance.setFunctionMissing(new MethodInvokerOnMissing()
                .setFunctionMissing(configEngine.getFunctionMissing())
                .setEnabled(Boolean.TRUE.equals(configEngine.getEnableMethodInvoke())));

        configEngine.setEngineInitializer(this);
    }

}
