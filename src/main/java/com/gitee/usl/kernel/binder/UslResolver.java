package com.gitee.usl.kernel.binder;

import java.lang.reflect.Parameter;

/**
 * @author hongda.li
 */
public interface UslResolver {

    Object resolve(Parameter parameter, ResolverContext context);
}
