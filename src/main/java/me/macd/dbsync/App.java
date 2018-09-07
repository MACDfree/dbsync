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
        TableStructCompare.compare("jdbc:mysql://192.168.111.111:3306/test", "root", "123456",
                "jdbc:sqlserver://192.168.111.111\\sql2008_demo;databaseName=test", "sa", "xxx");
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
