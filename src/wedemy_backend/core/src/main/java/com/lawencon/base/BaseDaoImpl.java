package com.lawencon.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;

import com.lawencon.util.Callback;

@Repository
public class BaseDaoImpl<T extends Serializable> {

	public Class<T> clazz;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.clazz = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseDaoImpl.class);
	}

	protected String getClassName() {
		return clazz.getSimpleName();
	}

	protected String getTableName() {
		return clazz.getAnnotation(Table.class).name();
	}

	protected T getById(final String id) throws Exception {
		return em().find(clazz, id);
	}
	
	protected T getById(final String id, Callback before) throws Exception {
		if (before != null)
			before.exec();
		
		return em().find(clazz, id);
	}

	protected List<T> getAll() {
		return em().createQuery("FROM " + clazz.getName(), clazz).getResultList();
	}

	protected void save(final T entity, Callback before, Callback after) throws Exception {
		if (before != null)
			before.exec();

		Method m = getBaseClass(entity).getDeclaredMethod("getId");
		Object objId = m.invoke(entity);

		if (objId != null) {
			em().merge(entity);
		} else {
			em().persist(entity);
		}
		if (after != null)
			after.exec();
	}

	protected void save(final T entity, Callback before, Callback after, boolean autoCommit) throws Exception {
		if (autoCommit) {
			begin();
			save(entity, before, after);
			commit();
		} else
			save(entity, before, after);
	}

	protected void save(final T entity, Callback before, Callback after, boolean autoCommit, boolean autoRollback)
			throws Exception {
		try {
			save(entity, before, after, autoCommit);
		} catch (Exception e) {
			if (autoRollback)
				rollback();

			throw new Exception(e);
		}
	}

	protected void delete(final T entity, Callback before, Callback after) throws Exception {
		if (before != null)
			before.exec();

		mainDelete(entity);

		if (after != null)
			after.exec();
	}

	protected void delete(final T entity, Callback before, Callback after, boolean autoCommit) throws Exception {
		if (autoCommit) {
			begin();
			delete(entity, before, after);
			commit();
		} else
			delete(entity, before, after);
	}

	protected void delete(final T entity, Callback before, Callback after, boolean autoCommit, boolean autoRollback)
			throws Exception {
		try {
			delete(entity, before, after, autoCommit);
		} catch (Exception e) {
			if (autoRollback)
				rollback();
		}
	}

	protected void deleteById(final Object entityId) throws Exception {
		T entity = null;
		if (entityId != null && entityId instanceof String) {
			entity = getById((String) entityId);
		}

		if (entity != null)
			delete(entity, null, null);
		else
			throw new Exception("ID Not Found");
	}

	private void mainDelete(final T entity) throws Exception {
		em().remove(entity);
	}

	protected boolean isIdExist(final String entityId) throws Exception {
		if (getById(entityId) == null) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean isIdExistAndActive(final String entityId) throws Exception {
		T data = getById(entityId);
		Field field;
		try {
			field = data.getClass().getSuperclass().getDeclaredField("isActive");
			field.setAccessible(true);
			if (field.get(data).equals(false)) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private Class<?> getBaseClass(final T entity) {
		Class<?> base = entity.getClass();
		try {
			base.getDeclaredField("id");
			return base;
		} catch (Exception e) {
			base = base.getSuperclass();
			while (true) {
				try {
					base.getDeclaredField("id");
					break;
				} catch (NoSuchFieldException x) {
					base = base.getSuperclass();
				}
			}
			return base;
		}
	}

	private EntityManager em() {
		return ConnHandler.getManager();
	}

	private void begin() {
		ConnHandler.begin();
	}

	private void commit() {
		ConnHandler.commit();
	}

	private void rollback() {
		ConnHandler.rollback();
	}

	protected <C> TypedQuery<C> createQuery(String sql, Class<C> clazz) throws Exception {
		return em().createQuery(sql, clazz);
	}

	protected Query createNativeQuery(String sql) throws Exception {
		return em().createNativeQuery(sql);
	}

	protected void updateNativeSQL(String sql, String whereCriteria, String updatedBy, String... criterias)
			throws Exception {
		StringBuilder sb = new StringBuilder(sql);
		sb.append(", updated_at = now() ");
		sb.append(", updated_by = ?" + (criterias.length + 1));
		sb.append(", version = (version + 1) ");
		sb.append(" WHERE id = ?" + (criterias.length + 2));

		Query q = em().createNativeQuery(sb.toString());
		for (int i = 0; i < criterias.length; i++) {
			q.setParameter(i + 1, criterias[i]);
		}

		q.setParameter(criterias.length + 1, updatedBy);
		q.setParameter(criterias.length + 2, whereCriteria);

		q.executeUpdate();
	}
}