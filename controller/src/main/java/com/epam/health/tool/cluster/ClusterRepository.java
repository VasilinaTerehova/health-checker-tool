package com.epam.health.tool.cluster;

import com.epam.health.tool.authenticate.impl.ClusterCredentials;
import com.epam.health.tool.model.Cluster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ClusterRepository {

    public List<Cluster> findClustersList() {
        try {
            String fileName = Paths.get(getClass().getClassLoader().getResource("clusters.json").toURI()).toString();

            JsonReader reader = new JsonReader(new FileReader(fileName));
            Type listType = new TypeToken<ArrayList<ClusterCredentials>>(){}.getType();
            ArrayList<ClusterCredentials> clusterListObject = new Gson().fromJson(reader, listType);
            return clusterListObject.stream().map(ClusterCredentials::getCluster).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println( e.getMessage() );
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
