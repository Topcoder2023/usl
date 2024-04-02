package com.manda.usl.kernel.bean;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.annotation.Import;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hongda.li
 */
public class BeanScanner {
    private final Class<?> root;

    public BeanScanner(Class<?> root) {
        this.root = root;
    }

    public Set<Class<?>> scan() {
        Set<Class<?>> result = new HashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackage(ClassUtil.getPackage(root), this::filter);
        classes.forEach(clz -> {
            result.add(clz);
            Class<?>[] imports = AnnotationUtil.getAnnotationValue(clz, Import.class);
            if (imports != null) {
                Arrays.stream(imports)
                        .map(BeanScanner::new)
                        .map(BeanScanner::scan)
                        .forEach(result::addAll);
            }
        });
        return result;
    }

    private boolean filter(Class<?> clz) {
        return AnnotationUtil.hasAnnotation(clz, Singleton.class) && ClassUtil.isNormalClass(clz);
    }
}
