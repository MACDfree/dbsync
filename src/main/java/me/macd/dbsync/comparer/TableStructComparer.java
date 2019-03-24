package me.macd.dbsync.comparer;

import me.macd.dbsync.constant.Context;
import me.macd.dbsync.diff.ColumnDiff;
import me.macd.dbsync.diff.impl.DefaultColumnDiff;
import me.macd.dbsync.domain.Column;
import me.macd.dbsync.domain.Table;
import me.macd.dbsync.loader.DataBaseLoader;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TableStructComparer {
    private String leftUrl;
    private String leftUserName;
    private String leftPassword;
    private String rightUrl;
    private String rightUserName;
    private String rightPassword;

    private TableStructComparer(
            String leftUrl, String leftUserName, String leftPassword,
            String rightUrl, String rightUserName, String rightPassword) {
        this.leftUrl = leftUrl;
        this.leftUserName = leftUserName;
        this.leftPassword = leftPassword;
        this.rightUrl = rightUrl;
        this.rightUserName = rightUserName;
        this.rightPassword = rightPassword;
    }

    public void compare() {
        new DataBaseLoader(leftUrl, leftUserName, leftPassword).load(Context.leftDataBase);
        new DataBaseLoader(rightUrl, rightUserName, rightPassword).load(Context.rightDataBase);

        // 使用map存储表相关信息，包括表名和此表中的字段信息
        // 源库中的表
        // map<tablename, table>
        Map<String, Table> leftTableMap = Context.leftDataBase.getTables();
        // 目标库中的表
        // map<tablename, table>
        Map<String, Table> rightTableMap = Context.rightDataBase.getTables();

        // 进行表比对，利用集合操作，得出差集
        Set<String> result = new LinkedHashSet<>();
        // 获取只在源库中存在的表
        result.clear();
        result.addAll(leftTableMap.keySet());
        result.removeAll(rightTableMap.keySet());
        System.out.println("只在左库中存在表个数：" + result.size());
        for (String key : result) {
            Context.onlyLeftTables.add(key);
            System.out.println("只在左库中存在：" + key);
        }
        System.out.println("------------------------------------------------------");
        // 获取只在目标库中存在的表
        result.clear();
        result.addAll(rightTableMap.keySet());
        result.removeAll(leftTableMap.keySet());
        System.out.println("只在右库中存在表个数：" + result.size());
        for (String key : result) {
            Context.onlyRightTables.add(key);
            System.out.println("只在右库中存在：" + key);
        }

        result.clear();
        Set<String> srcColumSet;
        Set<String> desColumSet;
        ColumnDiff compare = new DefaultColumnDiff();
        for (String tableName : leftTableMap.keySet()) {
            if (rightTableMap.containsKey(tableName)) {
                // 只有源库和目标库都存在的表的情况下才进行比较
                srcColumSet = leftTableMap.get(tableName).getColumns().keySet();
                desColumSet = rightTableMap.get(tableName).getColumns().keySet();

                // 只在源库中存在的字段
                result.clear();
                result.addAll(srcColumSet);
                result.removeAll(desColumSet);
                for (String key : result) {
                    System.out.println("只在左库中存在的字段");
                    System.out.println("表名：" + tableName + ", 字段名：" + key);
                    Context.onlyLeftColums.add(leftTableMap.get(tableName).getColumns().get(key));
                }

                // 只在目标库中存在的字段
                result.clear();
                result.addAll(desColumSet);
                result.removeAll(srcColumSet);
                for (String key : result) {
                    System.out.println("只在右库中存在的字段");
                    System.out.println("表名：" + tableName + ", 字段名：" + key);
                    Context.onlyRightColums.add(rightTableMap.get(tableName).getColumns().get(key));
                }

                for (String key : srcColumSet) {
                    if (desColumSet.contains(key)) {
                        // 只有左库和右库都存在的字段的情况下才进行比较
                        Column src = leftTableMap.get(tableName).getColumns().get(key);
                        Column des = rightTableMap.get(tableName).getColumns().get(key);

                        if (compare.diff(src, des)) {
                            Column[] cols = new Column[]{src, des};
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

    public String getLeftUrl() {
        return leftUrl;
    }

    public String getLeftUserName() {
        return leftUserName;
    }

    public String getLeftPassword() {
        return leftPassword;
    }

    public String getRightUrl() {
        return rightUrl;
    }

    public String getRightUserName() {
        return rightUserName;
    }

    public String getRightPassword() {
        return rightPassword;
    }

    public static class Builder {
        private String leftUrl;
        private String leftUserName;
        private String leftPassword;
        private String rightUrl;
        private String rightUserName;
        private String rightPassword;

        public TableStructComparer build() {
            TableStructComparer tableStructComparer = new TableStructComparer(
                    leftUrl, leftUserName, leftPassword, rightUrl, rightUserName, rightPassword);
            return tableStructComparer;
        }

        /**
         * 设置左侧数据库连接
         *
         * @param url
         * @param userName
         * @param password
         * @return
         */
        public Builder left(String url, String userName, String password) {
            this.leftUrl = url;
            this.leftUserName = userName;
            this.leftPassword = password;
            return this;
        }

        /**
         * 设置右侧数据库连接
         *
         * @param url
         * @param userName
         * @param password
         * @return
         */
        public Builder right(String url, String userName, String password) {
            this.rightUrl = url;
            this.rightUserName = userName;
            this.rightPassword = password;
            return this;
        }
    }
}
