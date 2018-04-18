package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.facade.model.service.DownloadableFileConstants;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;
import static com.epam.facade.model.service.DownloadableFileConstants.YarnProperties.YARN_NODEMANAGER_LOG_DIRS;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public abstract class CommonRuningClusterParamReceiver implements IRunningClusterParamReceiver {
    @Autowired
    protected HttpAuthenticationClient httpAuthenticationClient;

    @Autowired
    protected ClusterDao clusterDao;

    @Override
    public String getLogDirectory(String clusterName) throws InvalidResponseException {
        String logDirPropery = getPropertySiteXml(clusterDao.findByClusterName(clusterName), DownloadableFileConstants.ServiceFileName.YARN, YARN_NODEMANAGER_LOG_DIRS);

        System.out.println("log.dir: " + logDirPropery);
        return logDirPropery;
    }

    public HdfsNamenodeJson getHdfsNamenodeJson(ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException, CommonUtilException {
        String nameNodeUrl = getNameNodeUrl(clusterEntity);
        String url = "http://" + nameNodeUrl + "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo";

        System.out.println(url);
        String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterEntity.getClusterName(), url);
        System.out.println(answer);
        HdfsNamenodeJson hdfsUsageJson = CommonJsonHandler.get().getTypedValueFromInnerFieldArrElement(answer, HdfsNamenodeJson.class, "beans");
        System.out.println(hdfsUsageJson);
        return hdfsUsageJson;
    }

    public String getNameNodeUrl(ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException {
        String nameNodeUrl = getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS, DFS_NAMENODE_HTTP_ADDRESS);

        if (CheckingParamsUtil.isParamsNullOrEmpty(nameNodeUrl)) {
            return getHANameNodeUrl(clusterEntity.getClusterName());
        }

        return nameNodeUrl;
    }

    @Override
    public String getHANameNodeUrl(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        String haIds = getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS, "dfs.ha.namenodes." + clusterEntity.getClusterName());
        if (!CheckingParamsUtil.isParamsNullOrEmpty(haIds)) {
            return getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS,
                    DFS_NAMENODE_HTTP_ADDRESS + "." + clusterEntity.getClusterName() + "." + Arrays.stream(haIds.split(",")).findFirst()
                            .orElseThrow(() -> new InvalidResponseException("Can't find name node url for cluster - " + clusterEntity.getClusterName())));
        }

        throw new InvalidResponseException("Can't find name node url for cluster - " + clusterEntity.getClusterName());
    }

}
