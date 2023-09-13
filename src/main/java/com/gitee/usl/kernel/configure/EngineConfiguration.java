package com.gitee.usl.kernel.configure;

import com.googlecode.aviator.AviatorEvaluatorInstance;

/**
 * @author hongda.li
 */
public class EngineConfiguration {
    private AviatorEvaluatorInstance instance;

    public AviatorEvaluatorInstance getInstance() {
        return instance;
    }

    public EngineConfiguration setInstance(AviatorEvaluatorInstance instance) {
        this.instance = instance;
        return this;
    }
}
