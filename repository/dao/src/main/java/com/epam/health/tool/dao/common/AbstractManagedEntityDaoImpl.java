package com.epam.health.tool.dao.common;


import com.epam.health.tool.common.AbstractManagedEntity;

import java.io.Serializable;

/**
 * writeme: Should be the description of the class
 *
 * @author Vasilina Terehova
 */
public abstract class AbstractManagedEntityDaoImpl<E extends AbstractManagedEntity, ID extends Serializable> extends AbstractDaoImpl<E, Long>
    implements AbstractManagedEntityDao<E> {

}
