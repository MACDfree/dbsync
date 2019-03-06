package me.macd.dbsync.constant;

public enum DBType {
    mysql("mysql"), oracle("oracle"), mssql("mssql");

    String val;

    DBType(String val) {
        this.val = val;
    }

    public String val() {
        return this.val;
    }
}
