package me.macd.dbsync;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.macd.dbsync.domain.Column;
import me.macd.dbsync.enumerate.DBType;

public class Context {
    public static DBType leftDBType;
    public static DBType rightDBType;
    // 只在源库中存在的表
    public static List<String> onlyLeftTables = new ArrayList<>();
    // 只在目标库中存在的表
    public static List<String> onlyRightTables = new ArrayList<>();
    // 只在源库中存在的字段
    public static List<Column> onlyLeftColums = new ArrayList<>();
    // 只在目标库中存在的字段
    public static List<Column> onlyRightColums = new ArrayList<>();
    // 字段类型或长度不同的字段
    // map<tablename,list<>>
    public static Map<String, List<Column[]>> diffColums = new LinkedHashMap<>();

    // 只在源库中存在的数据
    public static List<Row> onlyLeftRows = new ArrayList<>();
    // 只在目标库中存在的数据
    public static List<Row> onlyRightRows = new ArrayList<>();

    // 有差异的数据
    public static Map<CompareTable, List<Row[]>> diffRows = new LinkedHashMap<>();
    // // 最终确认需要修改的源库字段
    // public static Map<String, List<Column>> alterSrcColums = new
    // LinkedHashMap<>();
    // // 最终确认需要修改的目标库字段
    // public static Map<String, List<Column>> alterDesColums = new
    // LinkedHashMap<>();
}
