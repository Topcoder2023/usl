package com.gitee.usl.infra.structure;

import com.gitee.usl.infra.utils.AnnotatedComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

/**
 * 元素类型唯一的集合
 * 相同的元素类型无法被重复添加
 *
 * @author hongda.li
 */
public class UniqueList<E> extends ArrayList<E> {

    public UniqueList() {
        super();
    }

    public UniqueList(int size) {
        super(size);
    }

    @Override
    public boolean add(E element) {
        Objects.requireNonNull(element);
        Class<?> type = element.getClass();
        if (this.stream().anyMatch(item -> Objects.equals(item.getClass(), type))) {
            return false;
        }
        boolean added = super.add(element);
        this.sort(Comparator.comparing(item -> AnnotatedComparator.getOrder(item.getClass())));
        return added;
    }

    @Override
    public void add(int index, E element) {
        Objects.requireNonNull(element);
        Class<?> type = element.getClass();
        if (this.stream().anyMatch(item -> Objects.equals(item.getClass(), type))) {
            return;
        }
        super.add(index, element);
        this.sort(Comparator.comparing(item -> AnnotatedComparator.getOrder(item.getClass())));
    }

}
