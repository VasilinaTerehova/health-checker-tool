package com.epam.health.tool.dao.common;

import com.epam.health.tool.common.AbstractEntity;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

public abstract class AbstractDaoImpl<E extends AbstractEntity<ID>, ID extends Serializable> implements AbstractDao<E, ID> {

  private static final Logger LOGGER = Logger.getLogger(AbstractDaoImpl.class);

  /**
   * Class of the entity, this DAO provides access to
   */
  private Class<E> persistentClass;

  public CriteriaBuilder createCriteriaBuilder() {
    return getEntityManager().getCriteriaBuilder();
  }

  /**
   * Sets the class of the entity, this DAO provides access to.
   *
   * @return Class of the entity
   */
  @SuppressWarnings("unchecked")
  public Class<E> getPersistentClass() {
    if (persistentClass == null) {
      Class<? extends AbstractDaoImpl> currentClass = getClass();
      if (currentClass.getGenericSuperclass() instanceof ParameterizedType) {
        //if this dao parametrized
        persistentClass = getPersistentClass(currentClass);
      } else {
        //otherwise search for first parent that is parametrized and get it parameter type
        Class parentClass = currentClass.getSuperclass();
        while (parentClass != null && !isParametrized(parentClass)) {
          parentClass = parentClass.getSuperclass();
        }
        if (!isParametrized(parentClass)) {
          throw new CommonHibernateException("Dao class(" + this.getClass().getCanonicalName() + ") in not correctly parametrized.");
        }
        persistentClass = getPersistentClass(parentClass);
      }
    }
    return persistentClass;
  }

  @SuppressWarnings("unchecked")
  private Class<E> getPersistentClass(Class<? extends AbstractDaoImpl> clazz) {
    Type persistentType = ((ParameterizedType) clazz.getGenericSuperclass())
        .getActualTypeArguments()[0];
    if (persistentType instanceof TypeVariable) {
      return (Class<E>) ((TypeVariable) persistentType).getBounds()[0];
    } else {
      return (Class<E>) persistentType;
    }
  }

  private boolean isParametrized(Class parentClass) {
    return (parentClass.getGenericSuperclass() instanceof ParameterizedType);
  }

  //protected abstract Session getSession();

  protected abstract EntityManager getEntityManager();


  /**
   * Encapsulates {@link org.hibernate.Session#getNamedQuery(String) criteria creation} from the user
   *
   * @param queryName the name of a query defined externally
   * @return Query
   * @throws org.hibernate.HibernateException
   * @see org.hibernate.Criteria#createCriteria(String, String)
   */
  protected Query getNamedQuery(String queryName) {
    return getEntityManager().createNamedQuery(queryName);
  }

  /**
   * Encapsulates {@link org.hibernate.Session#createQuery(String)} query creation} from the user
   *
   * @param queryString a HQL query
   * @return Query
   * @throws org.hibernate.HibernateException
   * @see org.hibernate.Criteria#createCriteria(String, String)
   */
  protected Query createQuery(String queryString) {
    return getEntityManager().createQuery(queryString);
  }


  /**
   * Encapsulates {@link org.hibernate.Session#createSQLQuery(String)} query creation} from the user
   *
   * @param queryString a HQL query
   * @return Query
   * @throws org.hibernate.HibernateException
   * @see org.hibernate.Criteria#createCriteria(String, String)
   */
  protected Query createSqlQuery(String queryString) {
    return getEntityManager().createNativeQuery(queryString);
  }

  /**
   * Encapsulates {@link org.hibernate.Session#createCriteria(String) criteria creation} from the user
   *
   * @param persistentClass a class, which is persistent, or has persistent subclasses
   * @return Criteria
   * @see org.hibernate.Criteria#createCriteria(String, String)
   */
  protected CriteriaQuery<E> createCriteria(Class persistentClass) {
    CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
    return criteriaBuilder.createQuery(persistentClass);
  }

  /**
   * Encapsulates {@link org.hibernate.Session#createCriteria(String, String) criteria creation} from the user
   *
   * @param persistentClass a class, which is persistent, or has persistent subclasses
   * @param alias           alias for the given class
   * @return Criteria
   * @see org.hibernate.Criteria#createCriteria(String, String)
   */
//  protected Cri createCriteria(Class persistentClass, String alias) {
//    CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//    CriteriaQuery query = criteriaBuilder.createQuery(persistentClass);
//    return query;
////    Join<E,AnotherEntity> join =
////        root.join(E.anotherEntity); //Step 2
////    return criteriaBuilder;
//  }

  /**
   * {@inheritDoc}
   */
  public void saveOrUpdate(E entity) {
    getEntityManager().persist(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void saveOrUpdateAll(Iterable<E> entities) {
    for (E entity : entities) {
      saveOrUpdate(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void update(E entity) {
    getEntityManager().merge(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void updateAll(Iterable<E> entities) {
    for (E entity : entities) {
      update(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void save(E entity) {
    EntityManager entityManager = getEntityManager();
    entityManager.persist(entity);
    //entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  public void saveAll(Iterable<E> entities) {
    for (E entity : entities) {
      save(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void delete(E entity) {
    getEntityManager().remove(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void deleteAll(Iterable<E> entities) {
    for (E entity : entities) {
      delete(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void refresh(E entity) {
    getEntityManager().refresh(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void refreshAll(Iterable<E> entities) {
    for (E entity : entities) {
      refresh(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void evict(E entity) {
    getEntityManager().detach(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void evictAll(Iterable<E> entities) {
    for (E entity : entities) {
      evict(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public E find(ID primaryKey) {
    return (E) getEntityManager().find(getPersistentClass(), primaryKey);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public List<E> findAll() {
    CriteriaQuery<E> query = getEntityManager().getCriteriaBuilder().createQuery(getPersistentClass());
    Root<E> from = query.from(getPersistentClass());
    query.select(from);
    TypedQuery<E> query1 = getEntityManager().createQuery(query);
    return query1.getResultList();
  }

  public void flush() {
    getEntityManager().flush();
  }

} 