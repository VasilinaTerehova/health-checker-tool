package com.epam.health.tool.dao.common;

import com.epam.health.tool.common.AbstractEntity;

import java.io.Serializable;
import java.util.List;

public interface AbstractDao<E extends AbstractEntity<PK>, PK extends Serializable> {

  /**
   * Saves new or updates existing Entity on the data layer
   *
   * @param entity the Entity to be saved or updated
   */
  void saveOrUpdate(E entity);

  /**
   * Saves new or updates collection of existing Entities on the data layer
   *
   * @param entities the collection of Entities to be saved or updated
   */
  void saveOrUpdateAll(Iterable<E> entities);

  /**
   * Updates the existing Entity on the data layer
   *
   * @param entity the Entity to be updated
   */
  void update(E entity);

  /**
   * Updates the collection of existing Entities on the data layer
   *
   * @param entities the collection of Entities to be updated
   */
  void updateAll(Iterable<E> entities);

  /**
   * Saves the Entity on the data layer
   *
   * @param entity the Entity to be deleted
   */
  void save(E entity);

  /**
   * Saves the collection of Entities on the data layer
   *
   * @param entities the collection of Entities to be deleted
   */
  void saveAll(Iterable<E> entities);

  /**
   * Deletes the Entity on the datalayer
   *
   * @param entity the Entity to be deleted
   */
  void delete(E entity);

  /**
   * Deletes the collection of Entities on the datalayer
   *
   * @param entities the collection of Entities to be deleted
   */
  void deleteAll(Iterable<E> entities);

  /**
   * Refreshes the Entity on the datalayer
   *
   * @param entity the Entity to be Refreshed
   */
  void refresh(E entity);

  /**
   * Refreshes the collection of Entities on the datalayer
   *
   * @param entities the collection of Entities to be Refreshed
   */
  void refreshAll(Iterable<E> entities);

  /**
   * Evictes the Entity on the datalayer
   *
   * @param entity the Entity to be Evicted
   */
  void evict(E entity);

  /**
   * Evictes the collection of Entities on the datalayer
   *
   * @param entities the collection of Entities to be Evicted
   */
  void evictAll(Iterable<E> entities);

  /**
   * Gets the Entity on the data layer by the primary key
   *
   * @param primaryKey primary key of the Entity
   * @return Entity or <code>NULL</code> in case when Entity was not found
   */
  E find(PK primaryKey);

  /**
   * Retrieves all Entities from the data layer
   *
   * @return List of entities
   */
  List<E> findAll();

  @Deprecated
  void flush();

}
