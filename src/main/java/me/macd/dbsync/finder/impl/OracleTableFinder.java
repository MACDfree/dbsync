package me.macd.dbsync.finder.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import me.macd.dbsync.Column;
import me.macd.dbsync.finder.TableFinder;

public class OracleTableFinder implements TableFinder {

    @Override
    public Map<String, Map<String, Column>> findTable(Connection conn) throws SQLException {
        Map<String, Map<String, Column>> map = new LinkedHashMap<>();
        String sql = "select b.TABLE_NAME,a.COLUMN_NAME,a.DATA_TYPE,a.CHAR_LENGTH,a.DATA_PRECISION,a.DATA_SCALE from user_tab_columns a right join user_tab_comments b on a.TABLE_NAME=b.table_name and b.table_type='TABLE' where a.COLUMN_NAME is not null";
        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            // int count = 0;
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (!map.containsKey(tableName.toLowerCase())) {
                    map.put(tableName.toLowerCase(), new LinkedHashMap<>());
                }
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("DATA_TYPE");
                if (columnName == null || "".equals(columnName)) {
                    continue;
                }
                Integer columnLen;
                Integer columnDigit;
                if ("NUMBER".equals(columnType)) {
                    columnLen = rs.getInt("DATA_PRECISION");
                    columnDigit = rs.getInt("DATA_SCALE");
                } else {
                    columnLen = rs.getInt("CHAR_LENGTH");
                    columnDigit = null;
                }
                map.get(tableName.toLowerCase()).put(columnName.toLowerCase(),
                        new Column(tableName, columnName, columnType.toLowerCase(), columnLen, columnDigit));
                // System.out.println(count++);
            }
        }

        return map;
    }

}
