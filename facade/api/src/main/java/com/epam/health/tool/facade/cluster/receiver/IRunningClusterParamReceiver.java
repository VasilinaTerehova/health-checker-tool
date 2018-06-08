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

package com.epam.health.tool.facade.cluster.receiver;

import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.facade.model.exception.InvalidResponseException;

import java.util.Set;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public interface IRunningClusterParamReceiver {
    String getLogDirectory(String clusterName) throws InvalidResponseException;
    String getYarnLocalDirectory(String clusterName) throws InvalidResponseException;
    String getPropertySiteXml( String clusterName, String siteName, String propertyName ) throws InvalidResponseException;
    Set<String> getLiveNodes(String clusterName ) throws InvalidResponseException;
    HdfsNamenodeJson getHdfsNamenodeJson( String clusterName ) throws InvalidResponseException;
    String getActiveResourceManagerAddress( String clusterName ) throws InvalidResponseException;
    String getNameNodeUrl( String clusterName ) throws InvalidResponseException;
}
