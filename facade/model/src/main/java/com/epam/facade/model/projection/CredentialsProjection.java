package com.epam.facade.model.projection;

import org.springframework.beans.factory.annotation.Value;

public interface CredentialsProjection {
    @Value( "#{target.username}" )
    String getUsername();
    @Value( "#{target.password}" )
    String getPassword();
}
