package com.manda.usl.data.sqlite;

import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import com.manda.usl.data.sqlite.mapper.ConditionBuilder;

import java.util.List;

/**
 * @author hongda.li
 */
public interface BaseMapper<E> {

    E findOne(E condition);

    List<E> find(E condition);

    Long findNumber(E condition);

    List<E> findBy(ConditionBuilder builder);

    PageResult<E> findPage(E condition, Page page);

    E insert(E entity);

    E update(E entity);

    E delete(E entity);

    void deleteAll();

    List<E> batchInsert(List<E> entityList);

    List<E> batchUpdate(List<E> entityList);

    List<E> batchDelete(List<E> entityList);
}
