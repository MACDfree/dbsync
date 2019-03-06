package me.macd.dbsync.domain;

import me.macd.dbsync.constant.DBType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据库实体
 * @author macd
 * @version 1.0 [2019-03-02 20:51]
 **/
public class DataBase {
    private String dataBaseName;
    private DBType dbType;
    private Map<String, Table> tables;
    private Map<String, View> views;
    private Map<String, Function> functions;

    public DataBase() {
        this.tables = new LinkedHashMap<>();
        this.views = new LinkedHashMap<>();
        this.functions = new LinkedHashMap<>();
    }

    public DataBase(DBType dbType) {
        this();
        this.dbType = dbType;
    }

    public void addTable(Table table) {
        this.tables.put(table.getTableName(), table);
    }

    public void addView(View view) {
    }

    public void addFunction(Function function) {
    }

    public Table getTable(String tableName) {
        return this.tables.get(tableName);
    }

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    public Map<String, Table> getTables() {
        return tables;
    }

    public Map<String, View> getViews() {
        return views;
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }
}
