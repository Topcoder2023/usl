package com.gitee.usl.function.db;

import cn.hutool.db.Entity;
import cn.hutool.db.PageResult;
import cn.hutool.db.sql.Condition;
import com.gitee.usl.USLRunner;
import com.gitee.usl.domain.Param;

import com.gitee.usl.infra.DatabaseConstant;
import com.gitee.usl.plugin.enhancer.FunctionMetaEnhancer;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.sqlite.core.NativeDB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作函数测试
 *
 * @author jingshu.zeng
 */


class DatabaseOperationFunctionTest {

    static USLRunner runner = new USLRunner()
            .configure(configuration -> configuration.put(DatabaseConstant.ENABLE_LOCK_KEY, true))
            .configure(configuration -> configuration.enhancer(new FunctionMetaEnhancer()))
            .configure(configuration -> configuration.loggerLevel(NativeDB.class.getName(), Level.TRACE));

    @Test
    void testExecuteInsert() {
        Object data = runner.run(new Param("db_execute_insert('insert into TEST values(?, ?, ?, ?)','3', 'user3','3','20')")).getData();
    }

    @Test
    void testUpdate() {
        // 构造更新数据和条件
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put("username", "new_user2");
        updateValues.put("age", 25); // 假设要将年龄更新为 25

        Map<String, Object> whereValues = new HashMap<>();
        whereValues.put("id", 2); // 假设条件是 id 为 2

        // 调用 db_update 方法
        Object data = runner.run(new Param("db_update('TEST', updateValues, whereValues)").addContext("updateValues", updateValues).addContext("whereValues", whereValues)).getData();

    }

    @Test
    void testFindAll() {
        // 调用查询全部方法
        List<Entity> entities = (List<Entity>) runner.run(new Param("db_find_all('TEST')")).getData();
        // 打印查询结果
        for (Entity entity : entities) {
            System.out.println(entity.toString());
        }
    }


    @Test
    void testFindByCondition() {
        String tableName = "TEST";
        Map<String, Object> conditionValues = new HashMap<>();
        conditionValues.put("username", "user1");
        conditionValues.put("age", 20);

        // 调用方法
        List<Entity> entities = (List<Entity>) runner.run(new Param("db_find_by_condition('TEST', conditionValues)").addContext("conditionValues", conditionValues)).getData();

        // 打印查询结果
        for (Entity entity : entities) {
            System.out.println(entity.toString());
        }
    }

    @Test
    void testQuery() {
        String sql = "SELECT * FROM TEST WHERE username = ?";
        String[] params = {"user1"};

        // 调用 query 方法
        List<Entity> resultList = (List<Entity>) runner.run(new Param("db_query('SELECT * FROM TEST WHERE username = ?','user1')")).getData();

        // 输出查询结果
        for (Entity entity : resultList) {
            System.out.println(entity.toString());
        }
    }


    @Test
    void testPage() {
        Map<String, Object> conditionValues = new HashMap<>();
        // 添加条件，如果有的话
        conditionValues.put("username", "user1");

        // 调用分页查询方法
        PageResult<Entity> pageResult = (PageResult<Entity>) runner.run(new Param("db_page('TEST', conditionValues, '1', '10')").addContext("conditionValues", conditionValues)).getData();

        for (Entity entity : pageResult) {
            System.out.println(entity.toString());
        }
        int total = pageResult.getTotal();
        System.out.println("Total records: " + total);
    }


    @Test
    void testFindLike() {
        // 指定表名
        String tableName = "TEST";
        // 指定列名
        String columnName = "username";
        // 指定关键字
        String keyword = "user1";
        // 指定模糊查询类型
        Condition.LikeType likeType = Condition.LikeType.Contains;
        // 调用模糊查询方法
        List<Entity> resultList = (List<Entity>) runner.run(new Param("db_find_like('" + tableName + "', '" + columnName + "', '" + keyword + "', '" + likeType + "')")).getData();
        // 输出查询结果
        for (Entity entity : resultList) {
            System.out.println(entity.toString());
        }
    }

    @Test
    void testFindIn() {
        // 调用 findIn 方法
        List<Entity> entities = (List<Entity>) runner.run(new Param("db_find_in('TEST', 'username', 'in user1,user3')")).getData();

        // 打印查询结果
        for (Entity entity : entities) {
            System.out.println(entity.toString());
        }
    }

}
