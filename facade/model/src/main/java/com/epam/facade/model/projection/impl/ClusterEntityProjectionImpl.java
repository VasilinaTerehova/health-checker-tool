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
