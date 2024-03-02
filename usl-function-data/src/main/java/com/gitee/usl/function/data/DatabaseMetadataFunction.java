package com.gitee.usl.function.data;

import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.exception.USLExecuteException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 *
 * 数据库元数据
 * @author jingshu.zeng
 */


@Slf4j
@FunctionGroup
public class DatabaseMetadataFunction {

    // 获取数据库名称
    @Function("get_database_name")
    public static String getDatabaseName(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        return metaData.getDatabaseProductName();
    }

    // 获取数据库版本号
    @Function("get_database_version")
    public static String getDatabaseVersion(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        return metaData.getDatabaseProductVersion();
    }

    // 获取数据库连接的 URL
    @Function("get_connection_url")
    public static String getConnectionURL(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        return metaData.getURL();
    }

    // 获取数据库驱动名称
    @Function("get_driver_name")
    public static String getDriverName(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        return metaData.getDriverName();
    }

    // 获取数据库驱动版本号
    @Function("get_driver_version")
    public static String getDriverVersion(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        return metaData.getDriverVersion();
    }

    // 获取数据库是否只允许读操作
    @Function("is_read_only")
    public static boolean isReadOnly(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        return metaData.isReadOnly();
    }

    // 获取数据库是否支持事务
    @Function("supports_transactions")
    public static boolean supportsTransactions(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        return metaData.supportsTransactions();
    }

    // 获取数据库元数据
    @Function("get_database_metadata")
    public static void getDatabaseMetadata(String url) {
        try {
            // 连接数据库
            Connection conn = DriverManager.getConnection(url);

            // 输出数据库元数据信息
            System.out.println("数据库名称: " + getDatabaseName(conn));
            System.out.println("数据库版本号: " + getDatabaseVersion(conn));
            System.out.println("连接URL: " + getConnectionURL(conn));
            System.out.println("驱动名称: " + getDriverName(conn));
            System.out.println("驱动版本号: " + getDriverVersion(conn));
            System.out.println("是否只读: " + isReadOnly(conn));
            System.out.println("是否支持事务: " + supportsTransactions(conn));

            // 关闭连接
            conn.close();
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }


    // 获取指定数据库中的所有表信息
    // 获取指定数据库中的所有表信息
    @Function("get_all_tables")
    public static void getAllTables(Connection conn, String databaseName) {
        try {
            // 获取数据库元数据
            DatabaseMetaData dbMetaData = conn.getMetaData();

            // 获取指定数据库中的所有表信息
            ResultSet tables = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});

            // 输出表信息，包括数据库名称
            while (tables.next()) {
                // 表名
                String tableName = tables.getString("TABLE_NAME");
                // 表类型
                String tableType = tables.getString("TABLE_TYPE");
                // 表备注
                String remarks = tables.getString("REMARKS");

                // 输出表信息，包括数据库名称
                System.out.println("数据库名称: " + databaseName);
                System.out.println("表名: " + tableName);
                System.out.println("类型: " + tableType);
                System.out.println("备注: " + remarks);
                System.out.println();
            }

            // 关闭结果集
            tables.close();
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }


    // 获取指定数据库表中的字段属性
    @Function("get_table_columns")
    public static void getTableColumns(Connection conn, String databaseName, String schema, String tableName) {
        try {
            // 获取数据库元数据
            DatabaseMetaData dbMetaData = conn.getMetaData();
            // 获取指定数据库表中的字段信息
            ResultSet columns = dbMetaData.getColumns(databaseName, schema, tableName, null);

            // 输出字段属性
            while (columns.next()) {
                // 表模式
                String tableSchema = columns.getString("TABLE_SCHEM");
                // 表名
                String table = columns.getString("TABLE_NAME");
                // 列名
                String columnName = columns.getString("COLUMN_NAME");
                // 数据类型
                String dataType = columns.getString("DATA_TYPE");
                // 字段备注
                String remarks = columns.getString("REMARKS");

                // 输出字段属性
                System.out.println("所属数据库: " + databaseName);
                System.out.println("表模式: " + tableSchema);
                System.out.println("表名: " + table);
                System.out.println("列名: " + columnName);
                System.out.println("数据类型: " + dataType);
                System.out.println("备注: " + remarks);
                System.out.println();
            }

            // 关闭结果集
            columns.close();
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }

    // 获取预编译 SQL 语句中占位符参数的个数
    @Function("get_parameter_count")
    public static int getParameterCount(String url, String username, String password, String sql) {
        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(url, username, password);

            // 创建 PreparedStatement 对象
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // 获取参数元数据
            ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();

            // 获取占位符参数的个数
            int parameterCount = parameterMetaData.getParameterCount();

            // 关闭连接
            preparedStatement.close();
            connection.close();

            return parameterCount;
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            return -1; // 返回负数表示获取参数个数失败
        }
    }

    // 获取查询结果集中列的元数据
    @Function("get_result_set_metadata")
    public static void getResultSetMetadata(String url, String username, String password, String sql, String parameterValue) {
        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(url, username, password);

            // 创建 PreparedStatement 对象
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // 设置参数值
            preparedStatement.setString(1, parameterValue);

            // 执行 SQL 查询语句
            ResultSet resultSet = preparedStatement.executeQuery();

            // 获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();

            // 获取查询字段数量
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                // 获取列名称
                String columnName = metaData.getColumnName(i);
                // 获取Java类型
                String columnClassName = metaData.getColumnClassName(i);
                // 获取SQL类型
                String columnTypeName = metaData.getColumnTypeName(i);
                System.out.println("列名称：" + columnName);
                System.out.println("Java类型：" + columnClassName);
                System.out.println("SQL类型：" + columnTypeName);
            }

            // 关闭连接
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }


    public static void main(String[] args) {
        // 输入 SQLite 数据库连接信息
        String url = "jdbc:sqlite:C:/Users/86151/USL/SYSTEM.db";

        // 测试获取数据库元数据
        System.out.println("数据库元数据：");
        getDatabaseMetadata(url);
        System.out.println();

        try {
            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url);

            // 测试获取指定数据库中的所有表信息
            System.out.println("指定数据库中的所有表信息：");
            getAllTables(conn, url);
            System.out.println();

            // 测试获取指定数据库表中的字段属性
            System.out.println("指定数据库表中的字段属性：");
            getTableColumns(conn, url, null, "TEST"); // 替换 your_table_name 为实际的表名
            System.out.println();

            // 关闭连接
            conn.close();
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }

        // 测试获取预编译 SQL 语句中占位符参数的个数
        String sql = "select * from TEST where id = ?";
        int parameterCount = getParameterCount(url, null, null, sql);
        System.out.println("预编译 SQL 语句中占位符参数的个数：" + parameterCount);

        // 测试获取查询结果集中列的元数据
        String querySql = "select * from TEST where id = ?";
        String parameterValue = "1";
        System.out.println("查询结果集中列的元数据：");
        getResultSetMetadata(url, null, null, querySql, parameterValue);
    }
}
