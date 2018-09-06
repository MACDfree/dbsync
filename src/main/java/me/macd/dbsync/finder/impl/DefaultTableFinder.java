package me.macd.dbsync.finder.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import me.macd.dbsync.Column;
import me.macd.dbsync.finder.TableFinder;

public class DefaultTableFinder implements TableFinder {

	@Override
	public Map<String, Map<String, Column>> findTable(Connection conn) throws SQLException {
		Map<String, Map<String, Column>> map = new LinkedHashMap<>();

		DatabaseMetaData metaData = conn.getMetaData();

		try (ResultSet tableResultSet = metaData.getTables(null, "%", "%", new String[] { "TABLE" })) {
			while (tableResultSet.next()) {
				String tableName = tableResultSet.getString("TABLE_NAME");
				try (ResultSet columnResultSet = metaData.getColumns(null, "%", tableName, "%")) {
					Map<String, Column> colums = new LinkedHashMap<>();
					while (columnResultSet.next()) {
						String columnName = columnResultSet.getString("COLUMN_NAME");
						String typeName = columnResultSet.getString("TYPE_NAME").toLowerCase();
						Integer columnSize = columnResultSet.getInt("COLUMN_SIZE");
						Integer digits = columnResultSet.getInt("DECIMAL_DIGITS");
						Column column = new Column(tableName, columnName, typeName, columnSize, digits);
						colums.put(columnName.toLowerCase(), column);
					}
					map.put(tableName.toLowerCase(), colums);
				} catch (SQLException e) {
					throw e;
				}
			}
		}
		
		return map;
	}

}
