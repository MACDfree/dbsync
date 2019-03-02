package me.macd.dbsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.macd.dbsync.diff.RowDiff;
import me.macd.dbsync.diff.impl.DefaultRowDiff;

public class RowCompare {

    private static List<CompareTable> parameters;

    static {
        parameters = new ArrayList<>();
        parameters.add(new CompareTable("frame_config", new String[] { "configname" }, new String[] { "configvalue" }));
    }

    public static void compare(String srcUrl, String srcUser, String srcPwd, String desStr, String desUser,
            String desPwd) {
        // map<CompareTable, map<key,row>>
        Map<CompareTable, Map<Key, Map<String, Object>>> srcMap = new HashMap<>();

        // map<CompareTable, map<key,row>>
        Map<CompareTable, Map<Key, Map<String, Object>>> desMap = new HashMap<>();

        try (Connection connSrc = DriverManager.getConnection(srcUrl, srcUser, srcPwd);
                Connection connDes = DriverManager.getConnection(desStr, desUser, desPwd);) {
            for (CompareTable ct : parameters) {
                try (Statement statement = connSrc.createStatement(); ResultSet rs = statement.executeQuery(ct.sql())) {
                    Map<Key, Map<String, Object>> mrow = new HashMap<>();
                    int columnCount = rs.getMetaData().getColumnCount();
                    while (rs.next()) {
                        Object[] keys = new Object[ct.getKeys().length];
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 0; i < keys.length; i++) {
                            keys[i] = rs.getString(ct.getKeys()[i]);
                        }

                        // 注意是从1开始的
                        for (int i = 1; i <= columnCount; i++) {
                            row.put(rs.getMetaData().getColumnLabel(i).toLowerCase(), rs.getObject(i));
                        }

                        mrow.put(new Key(keys), row);
                    }
                    srcMap.put(ct, mrow);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                try (Statement statement = connDes.createStatement(); ResultSet rs = statement.executeQuery(ct.sql())) {
                    Map<Key, Map<String, Object>> mrow = new HashMap<>();
                    int columnCount = rs.getMetaData().getColumnCount();
                    while (rs.next()) {
                        Object[] keys = new Object[ct.getKeys().length];
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 0; i < keys.length; i++) {
                            keys[i] = rs.getString(ct.getKeys()[i]);
                        }

                        // 注意是从1开始的
                        for (int i = 1; i <= columnCount; i++) {
                            row.put(rs.getMetaData().getColumnLabel(i).toLowerCase(), rs.getObject(i));
                        }

                        mrow.put(new Key(keys), row);
                    }
                    desMap.put(ct, mrow);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RowDiff diff = new DefaultRowDiff();

        for (CompareTable ct : parameters) {
            if (!srcMap.containsKey(ct) || !desMap.containsKey(ct)) {
                continue;
            }

            Set<Key> srcSet = srcMap.get(ct).keySet();
            Set<Key> desSet = desMap.get(ct).keySet();

            Set<Key> result = new LinkedHashSet<>();
            // 只在源库中存在的数据
            result.addAll(srcSet);
            result.removeAll(desSet);
            for (Key key : result) {
                Map<String, Object> row = srcMap.get(ct).get(key);
                Context.onlyLeftRows.add(new Row(ct.getTableName(), row));
            }

            // 只在目标库中存在的数据
            result.clear();
            result.addAll(desSet);
            result.removeAll(srcSet);
            for (Key key : result) {
                Map<String, Object> row = desMap.get(ct).get(key);
                Context.onlyRightRows.add(new Row(ct.getTableName(), row));
            }

            // 差异数据
            for (Key key : srcSet) {
                if (desSet.contains(key)) {
                    Map<String, Object> r1 = srcMap.get(ct).get(key);
                    Map<String, Object> r2 = desMap.get(ct).get(key);
                    if (diff.diff(ct, r1, r2)) {
                        Row[] rows = new Row[] { new Row(ct.getTableName(), r1), new Row(ct.getTableName(), r2) };
                        if (!Context.diffRows.containsKey(ct)) {
                            Context.diffRows.put(ct, new ArrayList<>());
                        }
                        Context.diffRows.get(ct).add(rows);
                    }
                }
            }
        }
    }

    public static class Key {
        private Object[] keys;

        public Key(Object... keys) {
            this.keys = keys;
        }

        @Override
        public int hashCode() {
            return keys[0].hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            Key key = (Key) obj;

            if (key.keys.length != this.keys.length) {
                return false;
            }

            for (int i = 0; i < this.keys.length; i++) {
                if (!this.keys[i].equals(key.keys[i])) {
                    return false;
                }
            }
            return true;
        }
    }
}
