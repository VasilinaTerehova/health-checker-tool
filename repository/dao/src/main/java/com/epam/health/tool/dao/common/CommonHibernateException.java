package com.epam.health.tool.dao.common;

/**
 * Base class for the Itision Common Hibernate exceptions
 *
 * @author Vasilina Terehova
 */
public class CommonHibernateException extends RuntimeException {

  public CommonHibernateException() {
  }

  public CommonHibernateException(String message) {
    super(message);
  }

  public CommonHibernateException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommonHibernateException(Throwable cause) {
    super(cause);
  }

}
