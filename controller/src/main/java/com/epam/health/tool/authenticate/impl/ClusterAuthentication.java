package com.epam.health.tool.authenticate.impl;

import com.epam.health.tool.authenticate.IHttpCredentials;
import com.epam.health.tool.authenticate.ISshCredentials;
import com.epam.health.tool.authenticate.BaseCredentialsToken;
import com.epam.health.tool.authenticate.IKerberosAuthentication;
import org.apache.http.client.CredentialsProvider;

import java.util.ArrayList;
import java.util.List;

public class ClusterAuthentication extends BaseCredentialsToken implements IHttpCredentials,
        ISshCredentials, IKerberosAuthentication {
    private CredentialsProvider credentialsProvider;
    private SshCredentials sshCredentials;
    private List<String> authShemes;
    private boolean kerberosAuth;

    public ClusterAuthentication() {
        credentialsProvider = null;
        sshCredentials = null;
        authShemes = new ArrayList<>();
        kerberosAuth = false;
    }

    @Override
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    @Override
    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    public List<String> getAuthShemes() {
        return authShemes;
    }

    @Override
    public void setAuthShemes(List<String> authShemes) {
        this.authShemes = authShemes;
    }

    @Override
    public SshCredentials getSshCredentials() {
        return sshCredentials;
    }

    @Override
    public void setSshCredentials(SshCredentials sshCredentials) {
        this.sshCredentials = sshCredentials;
    }

    @Override
    public void setKerberosAuth(boolean isKerberos) {
        this.kerberosAuth = isKerberos;
    }

    @Override
    public boolean isKerberosSet() {
        return kerberosAuth;
    }
}
