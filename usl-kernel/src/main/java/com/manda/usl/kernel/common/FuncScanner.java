package com.manda.usl.kernel.common;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.annotation.Func;
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
public interface FuncScanner {
    /**
     * 执行扫描
     *
     * @param scanner 扫描器
     */
    void doScan(Scanner scanner);

    @SuppressWarnings("UnusedReturnValue")
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
            ClassUtil.scanPackage(packageName, clz -> ClassUtil.isNormalClass(clz) && this.matchRunnerName(clz))
                    .stream()
                    .map(ReflectUtil::newInstanceIfPossible)
                    .filter(Objects::nonNull)
                    .forEach(groupList::add);

            return this;
        }

        public Scanner scan(Object bean) {
            Assert.notNull(bean, "bean must not be null");
            Class<?> clazz = bean.getClass();
            Assert.isTrue(AnnotationUtil.hasAnnotation(clazz, Func.class), "@Func annotation must exist");

            if (this.matchRunnerName(clazz)) {
                this.groupList.add(bean);
            }

            return this;
        }

        public Scanner scan(List<Object> beanList) {
            beanList.forEach(this::scan);
            return this;
        }

        private boolean matchRunnerName(Class<?> clazz) {
            return this.runnerName.equals(CharSequenceUtil.blankToDefault(AnnotationUtil.getAnnotationValue(clazz, Func.class), USLRunner.DEFAULT_NAME));
        }
    }
}
