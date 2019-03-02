package me.macd.dbsync.diff;

import me.macd.dbsync.domain.Column;

/**
 * 列比较抽象接口
 * 
 * @author macd
 *
 */
@FunctionalInterface
public interface ColumnDiff {

    /**
     * 列比较
     * 
     * @param c1
     * @param c2
     * @return
     */
    boolean diff(Column c1, Column c2);
}
