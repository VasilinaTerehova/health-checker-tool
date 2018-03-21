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
@Table(name = ClusterEntity.TABLE_NAME)
public class ClusterEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster";
    public static final String COLUMN_CLUSTER_TITLE = "title_";
    public static final String COLUMN_CLUSTER_NAME = "cluster_name_";
    public static final String COLUMN_CLUSTER_TYPE = "cluster_type_";
    public static final String COLUMN_HOST = "host_";
    public static final String COLUMN_IS_SECURED = "is_secured_";
    public static final String COLUMN_FK_HTTP_CREDENTIALS= HttpCredentialsEntity.TABLE_NAME;
    public static final String COLUMN_FK_SSH_CREDENTIALS= SshCredentialsEntity.TABLE_NAME;
    public static final String COLUMN_FK_KERBEROS_CREDENTIALS= KerberosCredentialsEntity.TABLE_NAME;

    @NotNull
    @Column(name = COLUMN_CLUSTER_TITLE)
    String title;

    @NotNull
    @Column(name = COLUMN_CLUSTER_NAME)
    String clusterName;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = COLUMN_CLUSTER_TYPE)
    private ClusterTypeEnum clusterTypeEnum;

    @OneToOne
    @JoinColumn(name=TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_HTTP_CREDENTIALS, unique=true, nullable=false, updatable=false)
    private HttpCredentialsEntity http;

    @OneToOne
    @JoinColumn(name=TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_SSH_CREDENTIALS, unique=true, nullable=false, updatable=false)
    private SshCredentialsEntity ssh;

    @OneToOne
    @JoinColumn(name=TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_KERBEROS_CREDENTIALS, unique=true, nullable=false, updatable=false)
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
