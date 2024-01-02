package com.gitee.usl.grammar;

import com.gitee.usl.grammar.asm.Script;
import com.googlecode.aviator.EnvProcessor;

import java.util.Map;

/**
 * @author hongda.li
 */
public class EmptyEnvProcessor implements EnvProcessor {
    private static final EnvProcessor INSTANCE = new EmptyEnvProcessor();

    public static EnvProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public void beforeExecute(Map<String, Object> env, Script script) {
        // do nothing
    }

    @Override
    public void afterExecute(Map<String, Object> env, Script script) {
        // do nothing
    }
}
