package com.epam.health_tool.controller;

import com.epam.health_tool.authenticate.Authenticator;
import com.epam.health_tool.authenticate.impl.ClusterCredentials;
import com.epam.health_tool.cluster.facade.ClusterFacade;
import com.epam.health_tool.cluster.facade.ClusterNotFoundException;
import com.epam.health_tool.model.YarnApplicationCdh;
import com.epam.health_tool.util.CommonUtilException;
import com.epam.health_tool.util.CommonUtilHolder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/7/2018.
 */
@RestController
public class ApplicationListService {
    @Autowired
    CommonUtilHolder commonUtilHolder;

    @Autowired
    private ClusterFacade clusterFacade;

    @Autowired
    private Authenticator authenticator;

    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping("/getApplicationList")
    public List<YarnApplicationCdh> greeting(@RequestParam(value = "clusterName", required = false, defaultValue = "cdh513") String clusterName, Model model) throws CommonUtilException, IOException, AuthenticationException, ClusterNotFoundException {
        ClusterCredentials clusterCredentials = clusterFacade.readClusterCredentials(clusterName);
        authenticator.authenticate(clusterCredentials);

        String url = "http://" + clusterCredentials.getCluster().getHost() + ":7180/api/v10/clusters/" + clusterCredentials.getCluster().getName() + "/services/yarn/yarnApplications?from=1";
        System.out.println(url);
        HttpUriRequest httpUriRequest = CommonUtilHolder.httpCommonUtilInstance().createHttpUriRequest(url);
        Type listType = new TypeToken<ArrayList<YarnApplicationCdh>>() {
        }.getType();
        String json = EntityUtils.toString(CommonUtilHolder.httpCommonUtilInstance().createHttpClient()
                .execute(httpUriRequest).getEntity());
        Gson gson = new Gson();
        ArrayList<YarnApplicationCdh> yarnApplicationCdhs = gson.fromJson(gson.fromJson(json, JsonObject.class).get("applications"), listType);
        System.out.println(yarnApplicationCdhs);
        return yarnApplicationCdhs;
    }

    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping("/killApplication")
    public boolean killApplication(@RequestParam(value = "clusterName", required = false, defaultValue = "cdh513") String clusterName,
                                   @RequestParam(value = "applicationId", required = false, defaultValue = "cdh513") String applicationId)
            throws CommonUtilException, IOException, AuthenticationException, ClusterNotFoundException {
        ClusterCredentials clusterCredentials = clusterFacade.readClusterCredentials(clusterName);
        authenticator.authenticate(clusterCredentials);

        String url = "http://" + clusterCredentials.getCluster().getHost() + ":7180/api/v10/clusters/"
                + clusterCredentials.getCluster().getName() + "/services/yarn/yarnApplications/"+applicationId+"/kill";
        System.out.println(url);
        HttpUriRequest httpUriRequest = CommonUtilHolder.httpCommonUtilInstance().createHttpUriRequest(url);
        CloseableHttpResponse execute = CommonUtilHolder.httpCommonUtilInstance().createHttpClient().execute(httpUriRequest);
        System.out.println(EntityUtils.toString(execute.getEntity()));
        System.out.println(execute.getStatusLine().getStatusCode());

        return true;
    }
}
