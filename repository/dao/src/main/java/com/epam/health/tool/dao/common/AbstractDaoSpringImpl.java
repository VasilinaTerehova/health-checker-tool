package com.epam.health.tool.dao.common;

import com.epam.health.tool.common.AbstractEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;

@Service
public abstract class AbstractDaoSpringImpl<E extends AbstractEntity<ID>, ID extends Serializable> extends AbstractDaoImpl<E, ID> {

  @PersistenceContext
  private EntityManager entityManager;

  public Object getSingleResult(Query query) {
    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  protected final EntityManager getEntityManager() {
    return entityManager;
  }
} 