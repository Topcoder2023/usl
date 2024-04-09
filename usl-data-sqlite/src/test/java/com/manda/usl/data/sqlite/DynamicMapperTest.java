package com.manda.usl.data.sqlite;

import com.manda.usl.data.sqlite.entity.DynamicEntity;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.bean.BeanContext;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author hongda.li
 */
class DynamicMapperTest {

    @Test
    void test() {
        USLRunner.run();

        DynamicMapper mapper = BeanContext.findFirst(DynamicMapper.class);

        DynamicEntity dynamic = new DynamicEntity();
        dynamic.put(DynamicEntity.FIELD_TABLE_NAME, "LOCK");
        List<DynamicEntity> entities = mapper.find(dynamic);
        System.out.println(entities);
    }
}