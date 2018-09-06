package me.macd.dbsync;

import java.util.HashMap;
import java.util.Map;

import me.macd.dbsync.enumerate.DBType;

/**
 * 对于不同数据库的字段类型进行统一映射
 * 
 * @author macd
 *
 */
public class TypeMap {
	public static Map<DBType, Map<String, String>> typeMap = new HashMap<>();

	static {
		Map<String, TypeMap> map = new HashMap<>();
		map.put("datetime", new TypeMap("datetime", "date", "datetime"));
		map.put("numeric", new TypeMap("numeric", "number", "decimal"));
		map.put("integer", new TypeMap("int", "number", "int"));
		map.put("ntext", new TypeMap("ntext", "clob", "text"));
		map.put("nvarchar", new TypeMap("nvarchar", "nvarchar2", "varchar"));
		map.put("image", new TypeMap("image", "blob", "blob"));

		typeMap.put(DBType.mssql, new HashMap<>());
		typeMap.put(DBType.mysql, new HashMap<>());
		typeMap.put(DBType.oracle, new HashMap<>());
		for (String key : map.keySet()) {
			TypeMap tm = map.get(key);
			typeMap.get(DBType.mssql).put(tm.mssql, key);
			typeMap.get(DBType.mysql).put(tm.mysql, key);
			typeMap.get(DBType.oracle).put(tm.oracle, key);
		}
		// 可能还有其他字段没有涵盖
		typeMap.get(DBType.mysql).put("longtext", "ntext");
		typeMap.get(DBType.mysql).put("float", "numeric");
		typeMap.get(DBType.oracle).put("number", "numeric");
	}

	String mssql;
	String oracle;
	String mysql;

	public TypeMap(String mssql, String oracle, String mysql) {
		super();
		this.mssql = mssql;
		this.oracle = oracle;
		this.mysql = mysql;
	}

	public String getMssql() {
		return mssql;
	}

	public String getOracle() {
		return oracle;
	}

	public String getMysql() {
		return mysql;
	}
}
