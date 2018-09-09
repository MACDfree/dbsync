package me.macd.dbsync.diff.impl;

import java.util.Map;

import me.macd.dbsync.CompareTable;
import me.macd.dbsync.diff.RowDiff;

public class DefaultRowDiff implements RowDiff {

    @Override
    public boolean diff(CompareTable table, Map<String, Object> r1, Map<String, Object> r2) {
        for (String columnName : table.getCompareColumns()) {
            if (r1.get(columnName) == null && r2.get(columnName) == null) {
                continue;
            } else if (r1.get(columnName) == null && r2.get(columnName) != null) {
                return true;
            } else if (!r1.get(columnName).equals(r2.get(columnName))) {
                return true;
            }
        }

        return false;
    }

}
