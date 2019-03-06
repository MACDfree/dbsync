package me.macd.dbsync.loader.impl;

import me.macd.dbsync.domain.Column;
import me.macd.dbsync.domain.DataBase;
import me.macd.dbsync.domain.Table;
import me.macd.dbsync.loader.Loader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class OracleLoader implements Loader {

    @Override
    public void loadTableStruct(Connection conn, DataBase dataBase) throws SQLException {
        Map<String, Map<String, Column>> map = new LinkedHashMap<>();
        String sql = "select b.TABLE_NAME,a.COLUMN_NAME,a.DATA_TYPE,a.CHAR_LENGTH,a.DATA_PRECISION,a.DATA_SCALE from user_tab_columns a right join user_tab_comments b on a.TABLE_NAME=b.table_name and b.table_type='TABLE' where a.COLUMN_NAME is not null";
        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("DATA_TYPE");
                if (columnName == null || "".equals(columnName)) {
                    continue;
                }

                Table table = dataBase.getTable(tableName);
                if (table == null) {
                    table = new Table(tableName);
                    dataBase.addTable(new Table(tableName));
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

                Column column = new Column(tableName, columnName, columnType.toLowerCase(), columnLen, columnDigit);
                table.addColumn(column);
            }
        }
    }

    @Override
    public void loadView(Connection conn, DataBase dataBase) throws SQLException {

    }

    @Override
    public void loadIndex(Connection conn, DataBase dataBase) throws SQLException {

    }

    @Override
    public void loadFunction(Connection conn, DataBase dataBase) throws SQLException {

    }
}
