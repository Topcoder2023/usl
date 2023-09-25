package com.gitee.usl.kernel.domain;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.infra.constant.NumberConstant;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author hongda.li
 */
public class FunctionHolder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, AviatorFunction> container;

    public FunctionHolder() {
        this.container = HashMap.newHashMap(NumberConstant.COMMON_SIZE);
    }

    public void register(AviatorFunction function) {
        Assert.notNull(function);
        String name = function.getName();

        if (this.container.containsKey(name)) {
            logger.warn("USL function has been registered - [{}]", name);
            return;
        }

        this.container.put(name, function);

        logger.info("Register USL function - [{}]", name);
    }

    public void onVisit(Consumer<AviatorFunction> consumer) {
        this.container.values().forEach(consumer);
    }

    public AviatorFunction search(String name) {
        AviatorFunction function = this.container.get(name);

        if (function == null) {
            logger.warn("USL function not found - [{}]", name);
        }

        return function;
    }

    public List<AviatorFunction> toList() {
        return new ArrayList<>(this.container.values());
    }
}
