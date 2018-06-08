/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.facade.cdh.service.status;

import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.facade.common.service.status.CommonServiceStatusReceiver;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhServiceStatusReceiver extends CommonServiceStatusReceiver {
    public static final String API_V10_CLUSTERS = ":7180/api/v10/clusters/";
    public static final String SERVICES = "/services";

    @Override
    public List<ServiceStatusHolder> getServiceStatusList(ClusterEntity clusterEntity) throws InvalidResponseException {
        try {
            String url = "http://" + clusterEntity.getHost() + API_V10_CLUSTERS + clusterEntity.getClusterName() + SERVICES;

            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterEntity.getClusterName(), url, false);

            return extractFromJsonString(answer);
        } catch (AuthenticationRequestException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    @Override
    public ServiceStatusHolder getServiceStatus(ClusterEntity clusterEntity, ServiceTypeEnum serviceTypeEnum) throws InvalidResponseException {
        try {
            String url = "http://" + clusterEntity.getHost() + API_V10_CLUSTERS + clusterEntity.getClusterName() + SERVICES + "/" + serviceTypeEnum.name().toLowerCase();

            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterEntity.getClusterName(), url, false);

            return CommonJsonHandler.get().getTypedValue(answer, ServiceStatus.class);
        } catch (CommonUtilException | AuthenticationRequestException e) {
            throw new InvalidResponseException("Can't extract status for cluster " + clusterEntity.getHost() + " for service " + serviceTypeEnum.name(), e);
        }
    }

    private List<ServiceStatusHolder> extractFromJsonString(String jsonString) throws InvalidResponseException {
        try {
            return new ArrayList<>(CommonJsonHandler.get().getListTypedValueFromInnerField(jsonString, ServiceStatus.class, "items"));
        } catch (CommonUtilException e) {
            throw new InvalidResponseException("Can't extract application list from answer - " + jsonString, e);
        }
    }
}
