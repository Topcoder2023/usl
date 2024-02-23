package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.api.Definable;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;
import com.gitee.usl.kernel.engine.FunctionDefinition;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE - 100)
public class DescriptionEnhancer extends AbstractFunctionEnhancer {
    public static final String FIELD_DESCRIPTION = "Description";

    @Override
    protected void enhanceDefinableFunction(Definable def) {
        FunctionDefinition definition = def.definition();
        AttributeMeta attribute = definition.getAttribute();

        MethodMeta<?> methodMeta = definition.getMethodMeta();

        Description description;
        if (methodMeta.method() != null) {
            description = AnnotationUtil.getAnnotation(methodMeta.method(), Description.class);
        } else if (methodMeta.targetType() != null) {
            description = AnnotationUtil.getAnnotation(methodMeta.targetType(), Description.class);
        } else {
            description = null;
        }

        if (description == null) {
            return;
        }

        attribute.put(FIELD_DESCRIPTION, description.value());
    }
}
