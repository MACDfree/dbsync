package me.macd.dbsync.domain;

import java.util.Set;

/**
 * 表实体
 * @author macd
 * @version 1.0 [2019-03-02 20:43]
 **/
public class Table {
    private String tableName;
    private Set<Column> columns;
    private Set<Index> indices;
}
