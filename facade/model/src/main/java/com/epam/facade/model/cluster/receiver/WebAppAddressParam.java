package com.epam.facade.model.cluster.receiver;

import com.epam.util.common.CheckingParamsUtil;

import java.util.function.Consumer;

public class WebAppAddressParam {
    private String webAppPrefix;
    private String sourceFileName;
    private String httpPrefix;
    private String clusterName;

    private WebAppAddressParam() {}

    public String getWebAppPrefix() {
        return webAppPrefix;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getHttpPrefix() {
        return httpPrefix;
    }

    public String getClusterName() {
        return clusterName;
    }

    public static class WebAppAddressParamBuilder {
        private WebAppAddressParam webAppAddressParam;

        private WebAppAddressParamBuilder() {
            this.webAppAddressParam = new WebAppAddressParam();
        }

        public static WebAppAddressParamBuilder get() {
            return new WebAppAddressParamBuilder();
        }

        public WebAppAddressParamBuilder withWebAppPrefix( String webAppPrefix ) {
            return addParam( webAppPrefix, ( param ) -> this.webAppAddressParam.webAppPrefix = param );
        }

        public WebAppAddressParamBuilder withSourceFileName( String sourceFileName ) {
            return addParam( sourceFileName, ( param ) -> this.webAppAddressParam.sourceFileName = param );
        }

        public WebAppAddressParamBuilder withHttpPrefix( String httpPrefix ) {
            return addParam( httpPrefix, ( param ) -> this.webAppAddressParam.httpPrefix = param );
        }

        public WebAppAddressParamBuilder withClusterName( String clusterName ) {
            return addParam( clusterName, ( param ) -> this.webAppAddressParam.clusterName = param );
        }

        public WebAppAddressParam build() throws InvalidBuildParamsException {
            assertParamsSetup();
            return this.webAppAddressParam;
        }

        private WebAppAddressParamBuilder addParam(String param, Consumer<String> addParamAction) {
            addParamWithCheck( param, addParamAction );

            return this;
        }

        private void addParamWithCheck( String param, Consumer<String> addParamAction ) {
            if (CheckingParamsUtil.isParamsNotNullOrEmpty( param )) {
                addParamAction.accept( param );
            }
        }

        private void assertParamsSetup() throws InvalidBuildParamsException {
            if ( CheckingParamsUtil.isParamsNullOrEmpty( this.webAppAddressParam.clusterName,
                    this.webAppAddressParam.sourceFileName, this.webAppAddressParam.webAppPrefix ) ) {
                throw new InvalidBuildParamsException( "Invalid params" );
            }
        }
    }
}
