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

package com.epam.facade.model.projection.impl;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.CredentialsProjection;
import com.epam.health.tool.model.ClusterTypeEnum;

public class ClusterEntityProjectionImpl implements ClusterEntityProjection {
    private Long id;
    private String name;
    private ClusterTypeEnum clusterType;
    private String host;
    private boolean secured;
    private CredentialsProjectionImpl http;
    private CredentialsProjectionImpl ssh;
    private CredentialsProjectionImpl kerberos;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ClusterTypeEnum getClusterType() {
        return clusterType;
    }

    public void setClusterType(ClusterTypeEnum clusterType) {
        this.clusterType = clusterType;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    @Override
    public CredentialsProjectionImpl getHttp() {
        return http;
    }

    public void setHttp(CredentialsProjectionImpl http) {
        this.http = http;
    }

    @Override
    public CredentialsProjectionImpl getSsh() {
        return ssh;
    }

    public void setSsh(CredentialsProjectionImpl ssh) {
        this.ssh = ssh;
    }

    @Override
    public CredentialsProjectionImpl getKerberos() {
        return kerberos;
    }

    public void setKerberos(CredentialsProjectionImpl kerberos) {
        this.kerberos = kerberos;
    }
}
