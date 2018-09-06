package me.macd.dbsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import me.macd.dbsync.diff.ColumnDiff;
import me.macd.dbsync.diff.impl.DefaultColumnDiff;
import me.macd.dbsync.enumerate.DBType;
import me.macd.dbsync.finder.TableFinder.TableFinderFactory;

public class TableStructCompare {

	public static void compare(String srcUrl, String srcUser, String srcPwd, String desStr, String desUser,
			String desPwd) {
		// 源库中的表
		// map<tablename, map<columnname,column>>
		Map<String, Map<String, Column>> srcMap;
		// 目标库中的表
		// map<tablename, map<columnname,column>>
		Map<String, Map<String, Column>> desMap;

		try (Connection connSrc = DriverManager.getConnection(srcUrl, srcUser, srcPwd);
				Connection connDes = DriverManager.getConnection(desStr, desUser, desPwd)) {
			// 获取源库的数据库类型
			String dbType = connSrc.getMetaData().getDatabaseProductName();
			Context.srcDBType = dbType(dbType);

			// 获取目标库的数据库类型
			dbType = connDes.getMetaData().getDatabaseProductName();
			Context.desDBType = dbType(dbType);

			// 查找源库中的所有表字段
			srcMap = TableFinderFactory.getTableFinder(Context.srcDBType).findTable(connSrc);

			// 查找目标库中所有表字段
			desMap = TableFinderFactory.getTableFinder(Context.desDBType).findTable(connDes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// 进行表比对，利用集合操作，得出差集
		Set<String> result = new LinkedHashSet<>();
		// 获取只在源库中存在的表
		result.clear();
		result.addAll(srcMap.keySet());
		result.removeAll(desMap.keySet());
		System.out.println("只在源库中存在表个数：" + result.size());
		for (String key : result) {
			Context.onlySrcTables.add(key);
			System.out.println("只在源库中存在：" + key);
		}
		System.out.println("------------------------------------------------------");
		// 获取只在目标库中存在的表
		result.clear();
		result.addAll(desMap.keySet());
		result.removeAll(srcMap.keySet());
		System.out.println("只在目标库中存在表个数：" + result.size());
		for (String key : result) {
			Context.onlyDesTables.add(key);
			System.out.println("只在目标库中存在：" + key);
		}

		result.clear();
		Set<String> srcColumSet;
		Set<String> desColumSet;
		ColumnDiff compare = new DefaultColumnDiff();
		for (String tableName : srcMap.keySet()) {
			if (desMap.containsKey(tableName)) {
				// 只有源库和目标库都存在的表的情况下才进行比较
				srcColumSet = srcMap.get(tableName).keySet();
				desColumSet = desMap.get(tableName).keySet();

				// 只在源库中存在的字段
				result.clear();
				result.addAll(srcColumSet);
				result.removeAll(desColumSet);
				for (String key : result) {
					System.out.println("只在源库中存在的字段");
					System.out.println("表名：" + tableName + ", 字段名：" + key);
					Context.onlySrcColums.add(srcMap.get(tableName).get(key));
				}

				// 只在目标库中存在的字段
				result.clear();
				result.addAll(desColumSet);
				result.removeAll(srcColumSet);
				for (String key : result) {
					System.out.println("只在目标库中存在的字段");
					System.out.println("表名：" + tableName + ", 字段名：" + key);
					Context.onlyDesColums.add(desMap.get(tableName).get(key));
				}

				for (String key : srcColumSet) {
					if (desColumSet.contains(key)) {
						// 只有源库和目标库都存在的字段的情况下才进行比较
						Column src = srcMap.get(tableName).get(key);
						Column des = desMap.get(tableName).get(key);

						if (compare.diff(src, des)) {
							Column[] cols = new Column[] { src, des };
							if (!Context.diffColums.containsKey(tableName)) {
								Context.diffColums.put(tableName, new ArrayList<>());
							}
							Context.diffColums.get(tableName).add(cols);
						}
					}
				}
			}
		}
	}

	private static DBType dbType(String dbType) {
		if ("mysql".equalsIgnoreCase(dbType)) {
			return DBType.mysql;
		} else if ("microsoft sql server".equalsIgnoreCase(dbType)) {
			return DBType.mssql;
		} else if ("oracle".equalsIgnoreCase(dbType)) {
			return DBType.oracle;
		}
		return DBType.mysql;
	}
}
