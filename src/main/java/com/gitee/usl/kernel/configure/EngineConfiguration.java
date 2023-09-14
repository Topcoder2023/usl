package com.gitee.usl.kernel.configure;

import com.gitee.usl.kernel.engine.UslScriptEngine;

/**
 * @author hongda.li
 */
public class EngineConfiguration {
    private UslScriptEngine scriptEngine;

    public UslScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public EngineConfiguration setScriptEngine(UslScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
        return this;
    }
}
