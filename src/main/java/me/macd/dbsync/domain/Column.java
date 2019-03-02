package me.macd.dbsync.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 列信息实体，描述需要用到的列信息
 * 
 * @author macd
 *
 */
public class Column {
    // 表名
    private String tableName;
    // 字段名称
    private String name;
    // 字段类型（原始数据库中的类型）
    private String type;
    // 字段长度
    private Integer size;
    // 字段精度
    private Integer digits;
    private Map<String, Object> extInfo = new LinkedHashMap<>();

    public Column(String tableName, String name, String type, Integer size, Integer digits) {
        super();
        this.tableName = tableName;
        this.name = name;
        this.type = type;
        this.size = size;
        this.digits = digits;
    }

    public Column(String tableName, String name, String type) {
        this(tableName, name, type, null, null);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Map<String, Object> getExtInfo() {
        return extInfo;
    }

    public Object putExtInfo(String key, Object val) {
        return this.extInfo.put(key, val);
    }

    public Object getExtInfo(String key) {
        return this.extInfo.get(key);
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    @Override
    public String toString() {
        return String.format("{tablename:%s,name:%s,type:%s,size:%s,digits:%s}", this.tableName, this.name, this.type,
                this.size, this.digits);
    }
}
