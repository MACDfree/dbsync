package me.macd.dbsync.domain;

import java.util.*;

/**
 * 表实体
 * @author macd
 * @version 1.0 [2019-03-02 20:43]
 **/
public class Table {
    private String tableName;
    private Map<String, Column> columns;
    private Map<String, Index> indices;

    public Table(String tableName) {
        // 表名统一用小写
        this.tableName = tableName.toLowerCase(Locale.CHINA);
        this.columns = new LinkedHashMap<>();
        this.indices = new LinkedHashMap<>();
    }

    public void addColumn(Column column) {
        this.columns.put(column.getName(), column);
    }

    public void addIndex(Index index) {
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, Column> getColumns() {
        return columns;
    }

    public Map<String, Index> getIndices() {
        return indices;
    }
}
