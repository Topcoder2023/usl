package com.manda.usl.manage.gui.mapper;

import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Direction;
import cn.hutool.db.sql.Order;
import com.manda.usl.data.sqlite.mapper.ConditionBuilder;
import com.manda.usl.manage.gui.entity.FileView;
import com.manda.usl.data.sqlite.BaseMapper;
import com.manda.usl.data.sqlite.annotation.Mapper;

import java.util.List;

/**
 * @author hongda.li
 */
@Mapper
public interface FileMapper extends BaseMapper<FileView> {

    default List<FileView> findFileByNameOrSuffix(String fileName, String suffix) {
        return this.findBy(ConditionBuilder.newBuilder()
                .andLike(FileView.FIELD_NAME, fileName, Condition.LikeType.Contains)
                .andLike(FileView.FIELD_SUFFIX, suffix, Condition.LikeType.Contains)
                .orderBy(new Order(FileView.FIELD_MODIFY, Direction.DESC))
                .orderBy(new Order(FileView.FIELD_SUFFIX, Direction.ASC)));
    }
}
