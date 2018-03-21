package com.epam.facade.model.projection;

import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.beans.factory.annotation.Value;

public interface ClusterEntityProjection {
    Long getId();
    @Value( "#{target.clusterName}" )
    String getName();
    @Value( "#{target.clusterTypeEnum}" )
    ClusterTypeEnum getClusterType();
    String getHost();
    boolean isSecured();
    @Value( "#{target.http}" )
    CredentialsProjection getHttp();
    @Value( "#{target.ssh}" )
    CredentialsProjection getSsh();
    @Value( "#{target.kerberos}" )
    CredentialsProjection getKerberos();
}
