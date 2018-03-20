package com.epam.health.tool.dao.common;


import com.epam.health.tool.common.AbstractManagedEntity;

/**
 * writeme: Should be the description of the class
 *
 * @author Vasilina Terehova
 */
public abstract class AbstractManagedEntityDaoSpringImpl<E extends AbstractManagedEntity> extends AbstractDaoSpringImpl<E, Long>
    implements AbstractDao<E,Long> {

}
