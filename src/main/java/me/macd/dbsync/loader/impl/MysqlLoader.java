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

public class MysqlLoader implements Loader {

    @Override
    public void loadTableStruct(Connection conn, DataBase dataBase) throws SQLException {
        Map<String, Map<String, Column>> map = new LinkedHashMap<>();
        String dbName = conn.getCatalog();
        String sql = "SELECT `TABLE_NAME`,`COLUMN_NAME`,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='"
                + dbName + "' and TABLE_NAME in (select TABLE_NAME FROM information_schema.tables where table_schema='"
                + dbName + "' and table_type='base table')";

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
                if ("int".equals(columnType) || "float".equals(columnType) || "decimal".equals(columnType)) {
                    columnLen = rs.getInt("NUMERIC_PRECISION");
                    columnDigit = rs.getInt("NUMERIC_SCALE");
                } else if ("longtext".equals(columnType) || "text".equals(columnType)
                        || "longblob".equals(columnType)) {
                    columnLen = null;
                    columnDigit = null;
                } else {
                    columnLen = rs.getInt("CHARACTER_MAXIMUM_LENGTH");
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
