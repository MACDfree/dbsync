package me.macd.dbsync.finder.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import me.macd.dbsync.Column;
import me.macd.dbsync.finder.TableFinder;

public class MysqlTableFinder implements TableFinder {

	@Override
	public Map<String, Map<String, Column>> findTable(Connection conn) throws SQLException {
		Map<String, Map<String, Column>> map = new LinkedHashMap<>();
		String dbName = conn.getCatalog();
		String sql = "SELECT TABLE_NAME,COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='"
				+ dbName + "' and TABLE_NAME in (select TABLE_NAME FROM information_schema.tables where table_schema='"
				+ dbName + "' and table_type='base table')";

		try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
			//int count = 0;
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
				map.get(tableName.toLowerCase()).put(columnName.toLowerCase(),
						new Column(tableName, columnName, columnType.toLowerCase(), columnLen, columnDigit));
				//System.out.println(count++);
			}
		}

		return map;
	}

}
