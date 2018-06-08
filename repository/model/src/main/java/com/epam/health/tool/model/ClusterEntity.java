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

package com.epam.health.tool.model;

import com.epam.health.tool.common.AbstractManagedEntity;
import com.epam.health.tool.model.credentials.HttpCredentialsEntity;
import com.epam.health.tool.model.credentials.KerberosCredentialsEntity;
import com.epam.health.tool.model.credentials.SshCredentialsEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
@Entity
@Table(name = ClusterEntity.TABLE_NAME, uniqueConstraints =
        {
                @UniqueConstraint(columnNames = ClusterEntity.COLUMN_CLUSTER_NAME)
        })
public class ClusterEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster";
    public static final String COLUMN_CLUSTER_TITLE = "title_";
    public static final String COLUMN_CLUSTER_NAME = "cluster_name_";
    public static final String COLUMN_CLUSTER_TYPE = "cluster_type_";
    public static final String COLUMN_HOST = "host_";
    public static final String COLUMN_IS_SECURED = "is_secured_";
    public static final String COLUMN_FK_HTTP_CREDENTIALS = HttpCredentialsEntity.TABLE_NAME;
    public static final String COLUMN_FK_SSH_CREDENTIALS = SshCredentialsEntity.TABLE_NAME;
    public static final String COLUMN_FK_KERBEROS_CREDENTIALS = KerberosCredentialsEntity.TABLE_NAME;

    @NotNull
    @Column(name = COLUMN_CLUSTER_TITLE)
    private String title;

    @NotNull
    @Column(name = COLUMN_CLUSTER_NAME)
    private String clusterName;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = COLUMN_CLUSTER_TYPE)
    private ClusterTypeEnum clusterTypeEnum;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_HTTP_CREDENTIALS, unique = true, nullable = false, updatable = true)
    private HttpCredentialsEntity http;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_SSH_CREDENTIALS, unique = true, nullable = false, updatable = true)
    private SshCredentialsEntity ssh;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_KERBEROS_CREDENTIALS, unique = true, nullable = true, updatable = true)
    private KerberosCredentialsEntity kerberos;

    @Column(name = COLUMN_IS_SECURED)
    private boolean secured;

    @Column(name = COLUMN_HOST)
    private String host;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public ClusterTypeEnum getClusterTypeEnum() {
        return clusterTypeEnum;
    }

    public void setClusterTypeEnum(ClusterTypeEnum clusterTypeEnum) {
        this.clusterTypeEnum = clusterTypeEnum;
    }

    public HttpCredentialsEntity getHttp() {
        return http;
    }

    public void setHttp(HttpCredentialsEntity http) {
        this.http = http;
    }

    public SshCredentialsEntity getSsh() {
        return ssh;
    }

    public void setSsh(SshCredentialsEntity ssh) {
        this.ssh = ssh;
    }

    public KerberosCredentialsEntity getKerberos() {
        return kerberos;
    }

    public void setKerberos(KerberosCredentialsEntity kerberos) {
        this.kerberos = kerberos;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
