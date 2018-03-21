package com.epam.health.tool.model.ddl;

import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.model.common.AbstractDataLoad;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.jackson.JsonNodeValueReader;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
public class Dataload extends AbstractDataLoad {

    static String propertiesFileName = "datasource";
    static String propertiesFolder = "spring-properties";

    public Dataload(String propertiesFileName, String propertiesFolder) {
        super(propertiesFileName, propertiesFolder);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length > 0) {
            propertiesFileName = args[0];
        }
        if (args.length > 1) {
            propertiesFolder = args[1];
        }
        //Dataload ddl = new Dataload(propertiesFileName, propertiesFolder);

        //recreateTablesCurrent();

        //ddl.recreateTables();
        readClusterCredentials("test");

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
        importClustersFromJson();
    }

    private void importClustersFromJson() {

    }

    public static void readClusterCredentials(String clusterName) throws IOException, FileNotFoundException, URISyntaxException {
        String fileName = null;
        fileName = Paths.get(Dataload.class.getClassLoader().getResource("clusters.json").toURI()).toString();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.getConfiguration().addValueReader(new JsonNodeValueReader());
        JsonNode orderNode = new ObjectMapper().readTree(new FileInputStream(fileName));
        JsonNode source = orderNode.get(2);
        System.out.println(source);
        modelMapper.createTypeMap(source, ClusterEntity.class).setPostConverter(context -> {
            context.getDestination().setClusterTypeEnum(ClusterTypeEnum.parse(context.getSource().get("cluster").get("type").asText()));
            return context.getDestination();
        });
        ClusterEntity order = modelMapper.map(source, ClusterEntity.class);
        System.out.println(order);
    }


}
