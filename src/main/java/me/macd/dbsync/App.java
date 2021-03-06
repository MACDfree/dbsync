package me.macd.dbsync;

import me.macd.dbsync.comparer.TableStructComparer;
import me.macd.dbsync.constant.Context;
import me.macd.dbsync.domain.Column;

public class App {
    static {
        try {
            // 注册数据库驱动类
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TableStructComparer.Builder builder = new TableStructComparer.Builder();
        builder.left("jdbc:mysql://192.168.211.68:3306/epointbid_zb_qy", "root", "123456")
                .right("jdbc:mysql://localhost:3307/bstool_zs", "root", "11111").build()
                .compare();
        
//        RowCompare.compare("jdbc:mysql://192.168.211.68:3306/epointbid_zb_qy", "root", "123456",
//                "jdbc:mysql://localhost:3307/bstool_zs", "root", "11111");
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
        
//        System.out.println("----------------------只在源库中存在-----------------------");
//        for (Row row : Context.onlyLeftRows) {
//            System.out.println(row);
//        }
//        System.out.println("----------------------只在目标库中存在-----------------------");
//        for (Row row : Context.onlyRightRows) {
//            System.out.println(row);
//        }
//        System.out.println("----------------------差异-----------------------");
//        for (CompareTable ct : Context.diffRows.keySet()) {
//            System.out.println(ct.getTableName());
//            for (Row[] rows : Context.diffRows.get(ct)) {
//                System.out.println(rows[0]);
//                System.out.println(rows[1]);
//            }
//        }
    }
}
