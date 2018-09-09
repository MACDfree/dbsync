package me.macd.dbsync;

import java.util.Map;

public class Row {
    private final String tableName;
    private final Map<String, Object> row;

    public Row(String tableName, Map<String, Object> row) {
        super();
        this.tableName = tableName;
        this.row = row;
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, Object> getRow() {
        return row;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tableName).append("]").append("{");

        for (Map.Entry<String, Object> entry : row.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(",");
        }
        sb.append("}");
        return sb.toString();
    }
}
