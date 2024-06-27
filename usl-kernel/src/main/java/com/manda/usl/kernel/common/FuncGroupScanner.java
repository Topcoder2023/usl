package com.manda.usl.kernel.common;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.kernel.annotation.FuncGroup;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 函数组加载接口
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface FuncGroupScanner {
    /**
     * 执行扫描
     *
     * @param scanner 扫描器
     */
    void doScan(Scanner scanner);

    @Getter
    final class Scanner {

        private final String runnerName;

        private final List<Object> groupList = new ArrayList<>();

        public Scanner(String runnerName) {
            this.runnerName = runnerName;
        }

        public Scanner scan(Class<?> clazz) {
            return scan(ClassUtil.getPackage(clazz));
        }

        public Scanner scan(String packageName) {
            ClassUtil.scanPackage(packageName, clz -> ClassUtil.isNormalClass(clz)
                            && AnnotationUtil.hasAnnotation(clz, FuncGroup.class)
                            && Objects.equals(runnerName, AnnotationUtil.getAnnotationValue(clz, FuncGroup.class)))
                    .stream()
                    .map(ReflectUtil::newInstanceIfPossible)
                    .filter(Objects::nonNull)
                    .forEach(groupList::add);

            return this;
        }

        public Scanner scan(Object bean) {
            Assert.notNull(bean, "bean must not be null");
            Class<?> clazz = bean.getClass();
            Assert.isTrue(AnnotationUtil.hasAnnotation(clazz, FuncGroup.class), "@FuncGroup annotation must exist");

            if (Objects.equals(this.runnerName, AnnotationUtil.getAnnotationValue(clazz, FuncGroup.class))) {
                this.groupList.add(bean);
            }

            return this;
        }

        public Scanner scan(List<Object> beanList) {
            beanList.forEach(this::scan);
            return this;
        }
    }
}
