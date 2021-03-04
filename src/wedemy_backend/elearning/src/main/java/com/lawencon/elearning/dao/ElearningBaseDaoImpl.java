package com.lawencon.elearning.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.lawencon.base.BaseDaoImpl;

public class ElearningBaseDaoImpl<T extends Serializable> extends BaseDaoImpl<T> {

	protected StringBuilder sqlBuilder(String... syntax) {
		StringBuilder sql = new StringBuilder();
		for (String builder : syntax) {
			sql.append(builder);
		}
		return sql;
	}

	protected T resultCheck(List<T> model) throws Exception {
		return Optional.ofNullable(model.size() > 0 ? model.get(0) : null).orElseThrow(Exception::new);
	}

	protected List<T> resultCheckList(List<T> list) throws Exception {
		return Optional.ofNullable(list.size() > 0 ? list : null).orElseThrow(Exception::new);
	}

}
