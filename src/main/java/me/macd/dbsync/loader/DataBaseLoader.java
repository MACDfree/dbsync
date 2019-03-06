package me.macd.dbsync.loader;

import me.macd.dbsync.domain.DataBase;
import me.macd.dbsync.constant.DBType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author macd
 * @version 1.0 [2019-03-06 21:11]
 **/
public class DataBaseLoader {
    private String url;
    private String userName;
    private String password;

    public DataBaseLoader() {
    }

    public DataBaseLoader(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public void load(DataBase dataBase) {
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            DBType dbType = dbType(connection);

            dataBase.setDbType(dbType);

            Loader loader = LoaderFactory.getLoader(dbType);

            // 加载数据表结构
            loader.loadTableStruct(connection, dataBase);

            // 加载视图
            loader.loadView(connection, dataBase);

            // 加载索引
            loader.loadIndex(connection, dataBase);

            // 加载函数
            loader.loadFunction(connection, dataBase);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DBType dbType(Connection connection) throws SQLException {
        String dbType = connection.getMetaData().getDatabaseProductName();

        if ("mysql".equalsIgnoreCase(dbType)) {
            return DBType.mysql;
        } else if ("microsoft sql server".equalsIgnoreCase(dbType)) {
            return DBType.mssql;
        } else if ("oracle".equalsIgnoreCase(dbType)) {
            return DBType.oracle;
        }
        throw new SQLException("未匹配数据库类型");
    }
}
