package com.epam.health.tool.facade.cdh.cluster;

import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.authentication.http.BaseHttpAuthenticatedAction;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.facade.common.service.DownloadableFileConstants;
import com.epam.health.tool.facade.common.service.RoleJson;
import com.epam.health.tool.facade.common.service.YarnRoleEnum;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.FileCommonUtil;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import com.epam.util.common.xml.XmlPropertyHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.YarnProperties.YARN_NODEMANAGER_LOG_DIRS;
import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.YarnProperties.YARN_RESOURCEMANAGER_WEBAPP_ADDRESS;

@Component("CDH-cluster")
public class CdhClusterSnapshotFacadeImpl extends CommonClusterSnapshotFacadeImpl {

    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    @Override
    public List<ServiceStatus> askForCurrentServicesSnapshot(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster(clusterName);
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services";

        String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url, false );

        return extractFromJsonString(answer);
    }

    private List<ServiceStatus> extractFromJsonString(String jsonString) throws InvalidResponseException {
        try {
            return CommonJsonHandler.get().getListTypedValueFromInnerField(jsonString, ServiceStatus.class, "items");
        } catch (CommonUtilException e) {
            throw new InvalidResponseException("Can't extract application list from answer - " + jsonString, e);
        }
    }
}
