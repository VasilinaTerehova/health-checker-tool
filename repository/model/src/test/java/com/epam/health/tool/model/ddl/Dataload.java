package com.epam.health.tool.model.ddl;

import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.model.common.AbstractDataLoad;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jackson.JsonNodeValueReader;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        Dataload ddl = new Dataload(propertiesFileName, propertiesFolder);

        //recreateTablesCurrent();

        ddl.recreateTables();
        //readClusterCredentials();

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
        importClustersFromJson(entityManager);
    }

    private void importClustersFromJson(EntityManager entityManager) {
        try {
            List<ClusterEntity> clusterEntities = readClusterCredentials();
            clusterEntities.forEach(entityManager::persist);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static List<ClusterEntity> readClusterCredentials() throws IOException, URISyntaxException {
        String fileName = null;
        fileName = Paths.get(Dataload.class.getClassLoader().getResource("clusters.json").toURI()).toString();

        JsonNode orderNode = new ObjectMapper().readTree(new FileInputStream(fileName));
        ArrayList<ClusterEntity> clusterEntities = new ArrayList<>();
        orderNode.iterator().forEachRemaining(jsonNode -> {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            modelMapper.getConfiguration().addValueReader(new JsonNodeValueReader());
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
            modelMapper.createTypeMap(jsonNode, ClusterEntity.class).setPostConverter(context -> {
                context.getDestination().setClusterTypeEnum(ClusterTypeEnum.parse(context.getSource().get("cluster").get("type").asText()));
                return context.getDestination();
            });
            //modelMapper.createTypeMap(jsonNode, ClusterEntity.class).set
            clusterEntities.add(modelMapper.map(jsonNode, ClusterEntity.class));
        });
        return clusterEntities;
    }


}
