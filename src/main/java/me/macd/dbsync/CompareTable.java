package me.macd.dbsync;

public class CompareTable {
    private final String tableName;
    private final String[] keys;
    private final String[] compareColumns;

    public CompareTable(String tableName, String[] keys, String[] compareColumns) {
        super();
        this.tableName = tableName;
        this.keys = keys;
        this.compareColumns = compareColumns;
    }

    public String sql() {
        String sql = "select * from " + tableName;
        return sql;
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getKeys() {
        return keys;
    }

    public String[] getCompareColumns() {
        return compareColumns;
    }

    @Override
    public int hashCode() {
        return this.tableName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        // 根据tablename和key进行比较
        CompareTable ct = (CompareTable) obj;

        if (!this.tableName.equals(ct.tableName)) {
            return false;
        }
        if (this.keys.length != ct.keys.length) {
            return false;
        }
        for (int i = 0; i < this.keys.length; i++) {
            if (!this.keys[i].equals(ct.keys[i])) {
                return false;
            }
        }
        return true;
    }
}
