/*
 * Copyright (c) 2008, Itision Corporation. All Rights Reserved.
 *
 * The content of this file is copyrighted by Itision Corporation and can not be
 * reproduced, distributed, altered or used in any form, in whole or in part.
 */
package com.epam.health.tool.model.common;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Class, that is used to create initial empty database for the Electronic Meeting System
 *
 * @author <a href="mailto: e.terehov@itision.com">Eugene Terehov</a>
 */
public abstract class AbstractDataLoad {

  private String databaseName;
  private String serverName;
  private Integer serverPort;
  private String user;
  private String password;
  private String driverClass;
  private String jdbcUrl;
  private String propertiesFileName;
  private String propertiesFolder;
  protected static EntityManager entityManager;

  public AbstractDataLoad(String propertiesFileName, String propertiesFolder) {
    this.propertiesFileName = propertiesFileName;
    this.propertiesFolder = propertiesFolder;
    ResourceBundle bundle = ResourceBundle.getBundle(propertiesFolder + "/" + this.propertiesFileName);
    databaseName = bundle.getString("jdbc.connection.name");
    serverName = bundle.getString("jdbc.connection.server.name");
    serverPort = Integer.valueOf(bundle.getString("jdbc.connection.server.port"));
    user = bundle.getString("jdbc.connection.username");
    password = bundle.getString("jdbc.connection.password");
    driverClass = bundle.getString("jdbc.driver.class");
    jdbcUrl = bundle.getString("jdbc.connection.url")
        .replace("${jdbc.connection.name}", databaseName)
        .replace("${jdbc.connection.server.name}", serverName)
        .replace("${jdbc.connection.server.port}", "" + serverPort)
        .replace("${jdbc.connection.username}", user)
        .replace("${jdbc.connection.password}", password);
  }

  protected void recreateDatabase() {
    Connection connection;

    try {
      // Load the JDBC driver.
      Class.forName(driverClass);

      // Establish the connection to the database.
      connection = DriverManager.getConnection(jdbcUrl, user, password);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("Database driver " + driverClass + " couldn't be found in the classpath.");
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }

//    try {
//      PreparedStatement preparedStatement = connection.prepareStatement(
//              new StringBuilder()
//                      .append("drop database ")
//                      .append(databaseName)
//                      .toString()
//      );
//      preparedStatement.execute();
//
//      PreparedStatement preparedStatement1 = connection.prepareStatement(
//              new StringBuilder()
//                      .append("create database ")
//                      .append(databaseName)
//                      .append(" DEFAULT CHARSET=utf8")
//                      .toString()
//      );
//      preparedStatement1.execute();
//    } catch (SQLException e) {
//      e.printStackTrace();
//      throw new RuntimeException("It seems that provided user doesn't have rights to drop/create database.");
//    }
  }

  protected abstract String[] getSpringConfigLocations();

  //protected abstract String getHibernateEntityManagerFactoryBeanName();

  protected abstract void createDatabaseData(EntityManager entityManager);

  public void recreateTables() {
    entityManager = getEntityManager();
    //entityManagerFactory.
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();


//    SessionFactory sessionFactory = (SessionFactory) staticApplicationContext.getBean(getHibernateSessionFactoryBeanName());
//    Session session = sessionFactory.openSession();
//
//    Transaction tx = session.beginTransaction();
//
//
    createDatabaseData(entityManager);
    transaction.commit();
  }

  public EntityManager getEntityManager() {
    if (entityManager == null) {
      EntityManagerFactory entityManagerFactory = getEntityManagerFactory();
      entityManager = entityManagerFactory.createEntityManager();
    }
    return entityManager;
  }

  private EntityManagerFactory getEntityManagerFactory() {
    StaticApplicationContext staticApplicationContext = new StaticApplicationContext();

    HashMap<String, Object> propertiesFileList = new HashMap<String, Object>();
    propertiesFileList.put("placeholderPrefix", "${");
    propertiesFileList.put("placeholderSuffix", "}");
    propertiesFileList.put("searchSystemEnvironment", "true");
    propertiesFileList.put("ignoreResourceNotFound", "true");
    propertiesFileList.put("locations", Arrays.asList(new String[]{propertiesFolder + "/" + propertiesFileName + ".properties", propertiesFolder + "/hibernate.properties"}));
    MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(propertiesFileList);
    GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
    genericBeanDefinition.setBeanClass(org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.class);
    genericBeanDefinition.setPropertyValues(mutablePropertyValues);
    staticApplicationContext.registerBeanDefinition("propertyContainer", genericBeanDefinition);

    for (String xmlFileName : getSpringConfigLocations()) {
      XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource(xmlFileName));

      for (String beanDefinitionName : xmlBeanFactory.getBeanDefinitionNames()) {
        staticApplicationContext.registerBeanDefinition(beanDefinitionName, xmlBeanFactory.getBeanDefinition(beanDefinitionName));
      }
    }

    staticApplicationContext.refresh();

    return (EntityManagerFactory) staticApplicationContext.getBean("entityManagerFactory");
  }
  //    Configuration configuration = new Configuration();
//    Configuration configure = configuration.configure();
//    SessionFactory sessionFactory = configure.buildSessionFactory();


}