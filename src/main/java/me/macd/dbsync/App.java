package me.macd.dbsync;

public class App {
	static {
		try {
			// 注册数据库驱动类
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TableStructCompare.compare("jdbc:mysql://192.168.211.135:3306/epointbid_sc_qy", "root", "123456",
				"jdbc:sqlserver://192.168.200.129\\sql2008_demo;databaseName=EpointBid_SC_BS", "sa", "Epoint@demo");
		int count = 0;
		for (String key : Context.diffColums.keySet()) {
			for (Column[] cols : Context.diffColums.get(key)) {
				System.out.println(cols[0]);
				System.out.println(cols[1]);
				System.out.println();
				count++;
			}
		}
		System.out.println(count);
	}
}
