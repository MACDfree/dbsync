package me.macd.dbsync.finder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import me.macd.dbsync.Column;
import me.macd.dbsync.enumerate.DBType;
import me.macd.dbsync.finder.impl.DefaultTableFinder;
import me.macd.dbsync.finder.impl.MysqlTableFinder;
import me.macd.dbsync.finder.impl.OracleTableFinder;

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

    public static class TableFinderFactory {
        public static TableFinder getTableFinder(DBType dbType) {
            if (dbType.equals(DBType.oracle)) {
                return new OracleTableFinder();
            } else if (dbType.equals(DBType.mysql)) {
                return new MysqlTableFinder();
            } else {
                return new DefaultTableFinder();
            }
        }
    }
}
