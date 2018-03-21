package com.epam.health.tool.dao.common;

import com.epam.health.tool.common.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * writeme: Should be the description of the class
 *
 * @author <a href="mailto:e.terehov@itision.com">Vasilina Terehova</a>
 */
public class DaoUtils {

  public static void addOrder(CriteriaQuery criteria, CriteriaBuilder criteriaBuilder, Expression expression, boolean ascending) {
    criteria.orderBy(ascending ? criteriaBuilder.asc(expression) : criteriaBuilder.desc(expression));
  }

  public static long count(CriteriaQuery criteria, CriteriaBuilder criteriaBuilder, EntityManager entityManager) {
    //criteria.setProjection(Projections.rowCount());
    CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
    Root<?> entityRoot = countCriteria.from(criteria.getResultType());
    countCriteria.select(criteriaBuilder.count(entityRoot));

    return entityManager.createQuery(countCriteria).getSingleResult();
  }

  /**
   * Use ${@link #count(javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder, javax.persistence.EntityManager)} instead of this one
   */
  @Deprecated
  public static int getCount(CriteriaQuery criteria, CriteriaBuilder criteriaBuilder, EntityManager entityManager) {
    CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
    Root<?> entityRoot = countCriteria.from(criteria.getResultType());
    countCriteria.select(criteriaBuilder.count(entityRoot));

    return entityManager.createQuery(countCriteria).getSingleResult().intValue();
  }

  public static List sublist(CriteriaQuery criteriaQuery, EntityManager entityManager, @Nullable Integer firstResult, @Nullable Integer maxResult) {
    TypedQuery query = entityManager.createQuery(criteriaQuery);
    if (firstResult != null && firstResult >= 0) {
      query.setFirstResult(firstResult);
    }

    if (maxResult != null && maxResult > 0) {
      query.setMaxResults(maxResult);
    }

    return query.getResultList();
  }

  public static List sublist(Query query, @Nullable Integer firstResult, @Nullable Integer maxResult) {
    if (firstResult != null && firstResult >= 0) {
      query.setFirstResult(firstResult);
    }

    if (maxResult != null && maxResult > 0) {
      query.setMaxResults(maxResult);
    }

    return query.getResultList();
  }

  /**
   * Use ${@link #sublist(javax.persistence.criteria.CriteriaQuery, javax.persistence.EntityManager, Integer, Integer)} instead of this one
   */
  @Deprecated
  public static List getSublist(CriteriaQuery criteriaQuery, EntityManager entityManager, int firstResult, int maxResult) {
    TypedQuery query = entityManager.createQuery(criteriaQuery);
    if (firstResult >= 0) {
      query.setFirstResult(firstResult);
    }

    if (maxResult > 0) {
      query.setMaxResults(maxResult);
    }

    return query.getResultList();
  }

  /**
   * Use ${@link #sublist(javax.persistence.Query, Integer, Integer)}  instead of this one
   */
  @Deprecated
  public static List getSublist(Query query, int firstResult, int maxResult) {
    if (firstResult >= 0) {
      query.setFirstResult(firstResult);
    }

    if (maxResult > 0) {
      query.setMaxResults(maxResult);
    }

    return query.getResultList();
  }

  public static List page(CriteriaQuery criteria, EntityManager entityManager, int pageNumber, int objectsPerPage) {
    if (pageNumber < 0) {
      throw new RuntimeException("pageNumber must be greater than 0");
    }
    if (objectsPerPage < 1) {
      throw new RuntimeException("objectsPerPage must be greater than 1");
    }

    return sublist(criteria, entityManager, objectsPerPage * pageNumber, objectsPerPage);
  }

  public static List page(Query query, int pageNumber, int objectsPerPage) {
    if (pageNumber < 0) {
      throw new RuntimeException("pageNumber must be greater than 0");
    }
    if (objectsPerPage < 1) {
      throw new RuntimeException("objectsPerPage must be greater than 1");
    }

    return sublist(query, objectsPerPage * pageNumber, objectsPerPage);
  }

  /**
   * Use ${@link #page(javax.persistence.criteria.CriteriaQuery, javax.persistence.EntityManager, int, int)}  instead of this one
   */
  @Deprecated
  public static List getPage(CriteriaQuery criteriaQuery, EntityManager entityManager, int objectsPerPage, int pageNumber) {
    TypedQuery query = entityManager.createQuery(criteriaQuery);
    if (objectsPerPage > 0) {
      query.setFirstResult(objectsPerPage * pageNumber);
      query.setMaxResults(objectsPerPage);
    }

    return query.getResultList();
  }

  /**
   * Use ${@link #page(javax.persistence.Query, int, int)}   instead of this one
   */
  @Deprecated
  public static List getPage(Query query, int objectsPerPage, int pageNumber) {
    if (objectsPerPage > 0) {
      query.setFirstResult(objectsPerPage * pageNumber);
      query.setMaxResults(objectsPerPage);
    }

    return query.getResultList();
  }

  public static CriteriaQuery addLikeRestriction(CriteriaQuery criteria, CriteriaBuilder criteriaBuilder, Expression expression, String value) {
    if (value != null && !value.equals("")) {
      criteria.where(criteriaBuilder.like(expression, "%" + value + "%"));
    }

    return criteria;
  }

  public static <E extends AbstractEntity<PK>, PK extends Serializable> Set<PK> extractIds(Set<E> entities) {
    Set<PK> ids = null;
    if (entities != null) {
      ids = new HashSet<PK>(entities.size());
      for (AbstractEntity<PK> abstractEntity : entities) {
        ids.add(abstractEntity.getId());
      }
    }

    return ids;
  }

}
