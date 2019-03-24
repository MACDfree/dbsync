package me.macd.dbsync.constant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Tab;
import me.macd.dbsync.CompareTable;
import me.macd.dbsync.Row;
import me.macd.dbsync.domain.Column;
import me.macd.dbsync.domain.DataBase;
import me.macd.dbsync.domain.Table;

public final class Context {
    public static DataBase leftDataBase = new DataBase();
    public static DataBase rightDataBase = new DataBase();

    // 只在左库中存在的表
    public static List<String> onlyLeftTables = new ArrayList<>();
    // 只在右库中存在的表
    public static List<String> onlyRightTables = new ArrayList<>();
    // 只在左库中存在的字段
    public static List<Column> onlyLeftColums = new ArrayList<>();
    // 只在右库中存在的字段
    public static List<Column> onlyRightColums = new ArrayList<>();
    // 字段类型或长度不同的字段
    // map<tablename,list<>>
    public static Map<String, List<Column[]>> diffColums = new LinkedHashMap<>();

    // 只在左库中存在的数据
    public static List<Row> onlyLeftRows = new ArrayList<>();
    // 只在右库中存在的数据
    public static List<Row> onlyRightRows = new ArrayList<>();

    // 有差异的数据
    public static Map<CompareTable, List<Row[]>> diffRows = new LinkedHashMap<>();
    // // 最终确认需要修改的左库字段
    // public static Map<String, List<Column>> alterSrcColums = new
    // LinkedHashMap<>();
    // // 最终确认需要修改的右库字段
    // public static Map<String, List<Column>> alterDesColums = new
    // LinkedHashMap<>();
}
