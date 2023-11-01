package com.gitee.usl.plugin.impl.sensitive;

import com.gitee.usl.infra.design.If;
import com.gitee.usl.infra.design.IfFactory;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.plugin.api.SensitizedStrategy;

import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public class SensitiveFactory extends IfFactory<Object, SensitiveContext> {
    protected SensitiveFactory() {
        super(ServiceSearcher.searchAll(SensitizedStrategy.class)
                .stream()
                .map(strategy -> (If<Object, SensitiveContext>) strategy)
                .collect(Collectors.toList()));
    }
}
