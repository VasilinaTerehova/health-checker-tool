package com.epam.health_tool.cluster.facade;

import com.epam.health_tool.authenticate.impl.ClusterCredentials;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Vasilina_Terehova on 3/6/2018.
 */
@Component
public class ClusterFacadeImpl implements ClusterFacade {
    @Override
    public ClusterCredentials readClusterCredentials(String clusterName) throws ClusterNotFoundException {
        String fileName = null;
        try {
            fileName = Paths.get(getClass().getClassLoader().getResource("clusters.json").toURI()).toString();

            JsonReader reader = new JsonReader(new FileReader(fileName));
            Type listType = new TypeToken<ArrayList<ClusterCredentials>>(){}.getType();
            ArrayList<ClusterCredentials> clusterListObject = new Gson().fromJson(reader, listType);
            Optional<ClusterCredentials> first = clusterListObject.stream().filter(clusterCredentials -> clusterCredentials.getCluster().getName().equals(clusterName)).findFirst();
            System.out.println(first);
            return first.get();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
        //new Gson().fromJson(
    }
}
