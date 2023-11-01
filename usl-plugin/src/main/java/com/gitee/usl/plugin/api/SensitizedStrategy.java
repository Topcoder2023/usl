package com.gitee.usl.plugin.api;

import com.gitee.usl.infra.design.If;
import com.gitee.usl.plugin.impl.sensitive.SensitiveContext;

/**
 * @author hongda.li
 */
public interface SensitizedStrategy extends If<Object, SensitiveContext> {
}
