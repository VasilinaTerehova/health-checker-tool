package com.epam.facade.model.projection;

import org.springframework.beans.factory.annotation.Value;

public interface ClusterIdsProjection {
    @Value( "#{target.id}" )
    Long getId();
    @Value( "#{target.http != null ? target.http.id : null}" )
    Long getHttpId();
    @Value( "#{target.ssh != null ? target.ssh.id : null}" )
    Long getSshId();
    @Value( "#{target.kerberos != null ? target.kerberos.id : null}" )
    Long getKerberosId();
}
