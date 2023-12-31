package com.gitee.usl.infra.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.constant.NumberConstant;

import java.util.List;
import java.util.Optional;

/**
 * @author hongda.li
 */
public class AnnotatedComparator {

    private AnnotatedComparator() {
    }

    /**
     * 根据元素类上的 @Order 注解值进行排序
     * 默认按从小到大的顺序排序
     *
     * @param elements 待排序的元素列表
     * @param <T>      元素的泛型
     */
    public static <T> void sort(List<T> elements) {
        sort(elements, false);
    }

    /**
     * 根据元素类上的 @Order 注解值进行排序
     * 正序排列时，按从小到大的顺序排序
     * 逆序排列时，按从大到小的顺序排序
     *
     * @param elements 待排序的元素列表
     * @param reverse  是否选择逆序
     * @param <T>      元素的泛型
     */
    public static <T> void sort(List<T> elements, boolean reverse) {
        elements.sort((element1, element2) -> {
            Class<?> clz1 = element1.getClass();
            Class<?> clz2 = element2.getClass();

            // 获取元素对象的类上的 @Order 注解中的 value值
            Integer value1 = AnnotatedComparator.getOrder(clz1);
            Integer value2 = AnnotatedComparator.getOrder(clz2);

            // 调用 Int 类型的比较方法
            // 默认按从小到大的顺序排序
            // 若选择逆序排列，则按从大到小的顺序排序
            return reverse ? value2.compareTo(value1) : value1.compareTo(value2);
        });
    }

    /**
     * 获取类上的 @Order 注解的值
     * 如果不存在 @Order 注解则给默认排序 0
     *
     * @param clz 任意类
     * @return 排序值
     */
    public static Integer getOrder(Class<?> clz) {
        return (Integer) Optional.ofNullable(AnnotationUtil.getAnnotationValue(clz, Order.class)).orElse(NumberConstant.ZERO);
    }
}
