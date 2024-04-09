package com.manda.usl.manage.service;

import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Direction;
import cn.hutool.db.sql.Order;
import com.manda.usl.data.sql.mapper.ConditionBuilder;
import com.manda.usl.kernel.annotation.Property;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.manage.entity.LogView;
import com.manda.usl.manage.mapper.LogMapper;

import java.util.List;

/**
 * @author hongda.li
 */
@Singleton
public class LogService {

    @Property
    private LogMapper logMapper;

    public List<LogView> list(String type, String time, String level) {
        return logMapper.findBy(ConditionBuilder.newBuilder()
                .andLike(LogView.FIELD_LEVEL, level, Condition.LikeType.Contains)
                .andLike(LogView.FIELD_TIME, time, Condition.LikeType.Contains)
                .andLike(LogView.FIELD_TYPE, type, Condition.LikeType.Contains)
                .orderBy(new Order(LogView.FIELD_TIME, Direction.DESC)));
    }
}
