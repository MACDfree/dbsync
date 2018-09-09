package me.macd.dbsync.diff;

import java.util.Map;

import me.macd.dbsync.CompareTable;

public interface RowDiff {
    boolean diff(CompareTable table, Map<String, Object> r1, Map<String, Object> r2);
}
