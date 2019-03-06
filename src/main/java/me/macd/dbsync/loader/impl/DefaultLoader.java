package me.macd.dbsync.loader.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.macd.dbsync.domain.Column;
import me.macd.dbsync.domain.DataBase;
import me.macd.dbsync.domain.Table;
import me.macd.dbsync.loader.Loader;

public class DefaultLoader implements Loader {

    @Override
    public void loadTableStruct(Connection conn, DataBase dataBase) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();

        try (ResultSet tableResultSet = metaData.getTables(null, "%", "%", new String[] { "TABLE" })) {
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");
                Table table = new Table(tableName);
                try (ResultSet columnResultSet = metaData.getColumns(null, "%", tableName, "%")) {
                    while (columnResultSet.next()) {
                        String columnName = columnResultSet.getString("COLUMN_NAME");
                        String typeName = columnResultSet.getString("TYPE_NAME").toLowerCase();
                        Integer columnSize = columnResultSet.getInt("COLUMN_SIZE");
                        Integer digits = columnResultSet.getInt("DECIMAL_DIGITS");
                        Column column = new Column(tableName, columnName, typeName, columnSize, digits);
                        table.addColumn(column);
                    }
                    dataBase.addTable(table);
                } catch (SQLException e) {
                    throw e;
                }
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
