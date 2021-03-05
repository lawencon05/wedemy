package com.lawencon.elearning.dao;

import java.io.Serializable;
import java.util.List;

import com.lawencon.base.BaseDaoImpl;

public class ElearningBaseDaoImpl<T extends Serializable> extends BaseDaoImpl<T> {

	protected StringBuilder sqlBuilder(String... syntax) {
		StringBuilder sql = new StringBuilder();
		for (String builder : syntax) {
			sql.append(builder);
		}
		return sql;
	}

	protected T resultCheck(List<T> list) throws Exception {
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	protected List<T> resultCheckList(List<T> list) throws Exception {
		return list.size() > 0 ? list : null;
	}

}
