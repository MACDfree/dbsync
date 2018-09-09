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
//        TableStructCompare.compare("jdbc:mysql://192.168.211.135:3306/epointbid_sc_qy", "root", "123456",
//                "jdbc:sqlserver://192.168.200.129\\sql2008_demo;databaseName=EpointBid_SC_BS", "sa", "Epoint@demo");
        
        RowCompare.compare("jdbc:mysql://192.168.211.68:3306/EpointBid_TP7_QY", "root", "123456",
                "jdbc:sqlserver://192.168.200.129\\sql2008_demo;databaseName=EpointBid_TP7_QY", "sa", "Epoint@demo");
//        int count = 0;
//        for (String key : Context.diffColums.keySet()) {
//            for (Column[] cols : Context.diffColums.get(key)) {
//                System.out.println(cols[0]);
//                System.out.println(cols[1]);
//                System.out.println();
//                count++;
//            }
//        }
//        System.out.println(count);
        
        System.out.println("----------------------只在源库中存在-----------------------");
        for (Row row : Context.onlySrcRows) {
            System.out.println(row);
        }
        System.out.println("----------------------只在目标库中存在-----------------------");
        for (Row row : Context.onlyDesRows) {
            System.out.println(row);
        }
        System.out.println("----------------------差异-----------------------");
        for (CompareTable ct : Context.diffRows.keySet()) {
            System.out.println(ct.getTableName());
            for (Row[] rows : Context.diffRows.get(ct)) {
                System.out.println(rows[0]);
                System.out.println(rows[1]);
            }
        }
    }
}
