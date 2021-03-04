package com.lawencon.base;

/**
 * @author lawencon05
 */
public class BaseServiceImpl {
	
//	@PersistenceContext
//	private EntityManager em;

	protected void begin() {
//		em.getTransaction().begin();
		ConnHandler.begin();
	}

	protected void commit() {
//		em.getTransaction().commit();
		ConnHandler.commit();
	}

	protected void rollback() {
//		em.getTransaction().rollback();
		ConnHandler.rollback();
	}

	protected void clear() {
		ConnHandler.clear();
	}
}
