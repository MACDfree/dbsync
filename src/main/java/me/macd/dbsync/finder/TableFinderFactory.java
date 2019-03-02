package me.macd.dbsync.finder;

import me.macd.dbsync.enumerate.DBType;
import me.macd.dbsync.finder.impl.DefaultTableFinder;
import me.macd.dbsync.finder.impl.MysqlTableFinder;
import me.macd.dbsync.finder.impl.OracleTableFinder;

/**
 * 查找数据中所有表及字段
 * @author macd
 * @version 1.0 [2019-03-02 20:38]
 **/
public class TableFinderFactory {
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
