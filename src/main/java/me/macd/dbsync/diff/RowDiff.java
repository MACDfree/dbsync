package me.macd.dbsync.diff;

import java.util.Map;

import me.macd.dbsync.CompareTable;

/**
 * 数据差异比对抽象
 * 
 * @author macd
 *
 */
public interface RowDiff {

    /**
     * 数据差异比对
     * 
     * @param table
     * @param r1
     * @param r2
     * @return
     */
    boolean diff(CompareTable table, Map<String, Object> r1, Map<String, Object> r2);
}
