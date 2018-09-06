package me.macd.dbsync.diff.impl;

import me.macd.dbsync.Column;
import me.macd.dbsync.Context;
import me.macd.dbsync.TypeMap;
import me.macd.dbsync.diff.ColumnDiff;

public class DefaultColumnDiff implements ColumnDiff {

	@Override
	public boolean diff(Column c1, Column c2) {
		String srcType = TypeMap.typeMap.get(Context.srcDBType).get(c1.getType());
		String desType = TypeMap.typeMap.get(Context.desDBType).get(c2.getType());

		if (srcType == null || desType == null) {
			return false;
		}

		if (!srcType.equals(desType) && !"numeric".equals(srcType) && !"integer".equals(srcType)
				&& !"numeric".equals(desType) && !"integer".equals(desType)) {
			return true;
		}

		if (!"nvarchar".equals(srcType)) {
			return false;
		}

		if (c1.getSize() == null || c2.getSize() == null) {
			return false;
		}

		if (!c1.getSize().equals(c2.getSize())) {
			return true;
		}

		if (c1.getDigits() == null || c2.getDigits() == null) {
			return false;
		}

		if (!c1.getDigits().equals(c2.getDigits())) {
			return true;
		}

		return false;
	}
}
