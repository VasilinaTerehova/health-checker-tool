package com.epam.util.http.header;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;

public interface IHeaderCreator {
    Header createHeader( HttpUriRequest httpUriRequest );
}
