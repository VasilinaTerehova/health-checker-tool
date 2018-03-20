package com.epam.health.tool.model.ddl;

import com.epam.health.tool.model.common.AbstractDataLoad;

import javax.persistence.EntityManager;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
public class Dataload extends AbstractDataLoad {

    static String propertiesFileName = "datasource";
    static String propertiesFolder = "spring-properties";

    public Dataload(String propertiesFileName, String propertiesFolder) {
        super(propertiesFileName, propertiesFolder);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            propertiesFileName = args[0];
        }
        if (args.length > 1) {
            propertiesFolder = args[1];
        }
        Dataload ddl = new Dataload(propertiesFileName, propertiesFolder);

        //recreateTablesCurrent();

        ddl.recreateTables();
    }

    protected String[] getSpringConfigLocations() {
        return new String[]{
                "spring-configuration/common-config-jpa.xml",
                "spring-configuration/common-config-datasource.xml",
        };
    }

    protected String getHibernateEntityManagerFactoryBeanName() {
        return "entityManagerFactory";
    }

    protected void createDatabaseData(EntityManager entityManager) {

    }
}
