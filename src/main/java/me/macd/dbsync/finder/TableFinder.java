package me.macd.dbsync.finder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import me.macd.dbsync.domain.Column;

/**
 * 抽象的查找数据库所有表字段的接口
 * 
 * @author macd
 *
 */
public interface TableFinder {

    /**
     * 查询指定数据库的所有表字段
     * 
     * @param conn
     * @return map&lt;tablename,map&lt;columnname,column&gt;&gt;
     * @throws SQLException
     */
    Map<String, Map<String, Column>> findTable(Connection conn) throws SQLException;
}
