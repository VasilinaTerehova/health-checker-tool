package com.epam.health.tool.common;

import com.epam.health.tool.util.HibernateUtils;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Abstract baseclass for all entities which using database identifier
 *
 * @author <a href="mailto: e.terehov@itision.com">Eugene Terehov</a>
 */
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

  public static final String COLUMN_ID = "id_";
  public static final String COLUMN_OBJ_VERSION = "obj_version_";

  public static final String DELIMITER_TABLE_INHERITANCE = "__";
  public static final String DELIMITER_INDEX = "___";

  /**
   * The id attribute used to uniquelly identify the instance of the subclass
   *
   * @return the unique id (primary key) of the instance
   */
  public abstract ID getId();

  /**
   * Two entities are equal if they have equal non-null ids and the same qualified classname. The implementation falls
   * back to reference equality when the id is null or the classnames differ.
   *
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    // we must deproxy objects before comparison, because hibernate proxy could be received here
    AbstractEntity<ID> thisEntity = HibernateUtils.deproxy(this);
    Object otherEntity = HibernateUtils.deproxy(obj);

    ID id = thisEntity.getId();

    if (id != null && otherEntity != null && otherEntity.getClass().getName().equals(getClass().getName())) {
      return id.equals(((AbstractEntity) otherEntity).getId());
    }

    // fallback to reference equality, original object instance should be used there
    return super.equals(obj);
  }

  /**
   * Hashcode is based on the instances id if any. Newly created objects without id uses the baseclass hashCode (based
   * on object reference).
   *
   * @return this object hashCode
   * @see Object#hashCode()
   */
  public int hashCode() {
    ID id = getId();

    if (id != null) {
      return id.hashCode();
    }

    return super.hashCode();
  }

}
