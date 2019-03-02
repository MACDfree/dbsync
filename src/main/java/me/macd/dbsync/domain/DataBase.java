package me.macd.dbsync.domain;

import java.util.Set;

/**
 * 数据库实体
 * @author macd
 * @version 1.0 [2019-03-02 20:51]
 **/
public class DataBase {
    private Set<Table> tables;
    private Set<View> views;
    private Set<Function> functions;
}
