package com.together.demo.dao;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * 动态jpql/nativesql查询的实现类
 * 创建者
 * 创建时间	2018年3月8日
 */
@Repository
public class DynamicDaoImpl implements DynamicDao {

	Logger logger = LoggerFactory.getLogger(DynamicDaoImpl.class);

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void save(Object entity) {
		em.persist(entity);
	}

	@Override
	public void update(Object entity) {
		em.merge(entity);
	}

	@Override
	public <T> void delete(Class<T> entityClass, Object entityid) {
		delete(entityClass, new Object[] { entityid });
	}

	@Override
	public <T> void delete(Class<T> entityClass, Object[] entityids) {
		for (Object id : entityids) {
			em.remove(em.getReference(entityClass, id));
		}
	}
	private Query createNativeQuery(String sql, Object... params) {
		Query q = em.createNativeQuery(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i + 1, params[i]);
			}
		}
		logger.info("query:{}",q);
		return q;
	}

	/**
	 * 重写一个带分页功能的
	 * @param sql
	 * @param params
	 * @return
	 */
	private Query createNativeQuery(String sql,Integer start,Integer size, Object... params) {
		Query q = em.createNativeQuery(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i + 1, params[i]);
			}
		}
		if(start!=null && size!=null) {
			q.setFirstResult((start - 1) * size);
			q.setMaxResults(size);
		}
		logger.info("query:{}",q);
		return q;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> nativeQueryList(String nativeSql, Object... params) {
		Query q = createNativeQuery(nativeSql, params);
		q.unwrap(SQLQuery.class).setResultTransformer(Transformers.TO_LIST);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> nativeQueryListModel(Class<T> resultClass,
			String nativeSql, Object... params) {
		Query q = createNativeQuery(nativeSql, params);;
		q.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(resultClass));
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> nativeQueryListMap(String nativeSql, Object... params) {
		Query q = createNativeQuery(nativeSql, params);
		q.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q.getResultList();
	}

	@Override
	public <T> List<T> nativeQueryListMapForPages(String nativeSql,Integer start,Integer size , Object... params) {
		Query q = createNativeQuery(nativeSql,start,size, params);
		q.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q.getResultList();
	}
	
	@Override
	public Object nativeQueryObject(String nativeSql, Object... params) {
		return createNativeQuery(nativeSql, params).getSingleResult();
	}
	@Override
	public int nativeExecuteUpdate(String nativeSql, Object... params) {
		return createNativeQuery(nativeSql, params).executeUpdate();
	}

	@Override
	public Object[] nativeQueryArray(String nativeSql, Object... params) {
		return (Object[]) createNativeQuery(nativeSql, params).getSingleResult();
	}

}
