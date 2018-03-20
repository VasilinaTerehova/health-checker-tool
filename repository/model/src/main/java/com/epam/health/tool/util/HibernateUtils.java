package com.epam.health.tool.util;

import org.hibernate.proxy.HibernateProxy;

/**
 * writeme: Should be the description of the class
 *
 * @author <a href="mailto:e.terehov@itision.com">Eugene Terehov</a>
 */
public class HibernateUtils {

  @SuppressWarnings("unchecked")
  public static <T> T deproxy(Object maybeProxy) {
    if (maybeProxy instanceof HibernateProxy) {
      return (T) ((HibernateProxy) maybeProxy).getHibernateLazyInitializer().getImplementation();
    }
    return (T) maybeProxy;
  }

}
