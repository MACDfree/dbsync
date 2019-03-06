package me.macd.dbsync.loader;

import me.macd.dbsync.constant.DBType;
import me.macd.dbsync.loader.impl.DefaultLoader;
import me.macd.dbsync.loader.impl.MysqlLoader;
import me.macd.dbsync.loader.impl.OracleLoader;

/**
 * 查找数据中所有表及字段
 * @author macd
 * @version 1.0 [2019-03-02 20:38]
 **/
public class LoaderFactory {
    public static Loader getLoader(DBType dbType) {
        if (dbType.equals(DBType.oracle)) {
            return new OracleLoader();
        } else if (dbType.equals(DBType.mysql)) {
            return new MysqlLoader();
        } else {
            return new DefaultLoader();
        }
    }
}
