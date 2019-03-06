package me.macd.dbsync.loader;

import me.macd.dbsync.domain.DataBase;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 抽象的加载数据库所有表字段的接口
 * 
 * @author macd
 *
 */
public interface Loader {
    /**
     * 加载指定数据库的所有表及字段
     * @param conn
     * @param dataBase
     * @throws SQLException
     */
    void loadTableStruct(Connection conn, DataBase dataBase) throws SQLException;

    void loadView(Connection conn, DataBase dataBase) throws SQLException;

    void loadIndex(Connection conn, DataBase dataBase) throws SQLException;

    void loadFunction(Connection conn, DataBase dataBase) throws SQLException;
}
