package com.gitee.usl.kernel.engine.provider;

import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
public class AviatorUslFunctionProvider extends AbstractUslFunctionProvider {
    @Override
    protected List<UslFunctionDefinition> class2Definition(Class<?> clz) {
        return Collections.emptyList();
    }

    @Override
    protected boolean filter(Class<?> clz) {
        return AviatorFunction.class.isAssignableFrom(clz);
    }
}
