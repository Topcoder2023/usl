package com.manda.usl.data.sqlite.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.LogicalOperator;
import cn.hutool.db.sql.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hongda.li
 */
public class ConditionBuilder {

    private final List<Order> orders = new ArrayList<>();

    private final List<Condition> conditionList = new ArrayList<>();

    private ConditionBuilder() {
    }

    public static ConditionBuilder newBuilder() {
        return new ConditionBuilder();
    }

    public ConditionBuilder andEq(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, "=", value);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andGt(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, ">", value);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andGte(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, ">=", value);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andLt(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, "<", value);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andLte(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, "<=", value);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andIn(String column, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return this;
        }
        Condition condition = new Condition(column, "IN", collection);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andNotIn(String column, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return this;
        }
        Condition condition = new Condition(column, "NOT IN", collection);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andLike(String column, String value, Condition.LikeType likeType) {
        if (CharSequenceUtil.isBlank(value)) {
            return this;
        }
        Condition condition = new Condition(column, value, likeType);
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andIsNull(String column) {
        Condition condition = new Condition();
        condition.setField(column);
        condition.setOperator("IS NULL");
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder andIsNotNull(String column) {
        Condition condition = new Condition();
        condition.setField(column);
        condition.setOperator("IS NOT NULL");
        condition.setLinkOperator(LogicalOperator.AND);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orEq(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, "=", value);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orGt(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, ">", value);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orGte(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, ">=", value);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orLt(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, "<=", value);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orLte(String column, Object value) {
        if (value == null) {
            return this;
        }
        Condition condition = new Condition(column, "<=", value);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orIn(String column, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return this;
        }
        Condition condition = new Condition(column, "IN", collection);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orNotIn(String column, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return this;
        }
        Condition condition = new Condition(column, "NOT IN", collection);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orLike(String column, String value, Condition.LikeType likeType) {
        if (CharSequenceUtil.isBlank(value)) {
            return this;
        }
        Condition condition = new Condition(column, value, likeType);
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orIsNull(String column) {
        Condition condition = new Condition();
        condition.setField(column);
        condition.setOperator("IS NULL");
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orIsNotNull(String column) {
        Condition condition = new Condition();
        condition.setField(column);
        condition.setOperator("IS NOT NULL");
        condition.setLinkOperator(LogicalOperator.OR);
        conditionList.add(condition);
        return this;
    }

    public ConditionBuilder orderBy(Order order) {
        orders.add(order);
        return this;
    }

    public Condition[] toArray() {
        return conditionList.toArray(new Condition[]{});
    }

    public Order[] getOrders() {
        if (CollUtil.isEmpty(orders)) {
            return new Order[]{};
        } else {
            // 驼峰转下划线
            orders.forEach(order -> order.setField(CharSequenceUtil.toUnderlineCase(order.getField())));
            return orders.toArray(new Order[]{});
        }
    }
}
