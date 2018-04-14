package com.epam.util.http.header;

import com.epam.util.common.CommonUtilException;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

public interface IHeaderCreator {
    Header createHeader(HttpUriRequest httpUriRequest, HttpContext httpClientContext) throws CommonUtilException;
}
