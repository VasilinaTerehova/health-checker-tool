package com.epam.health.tool.connector;

import com.epam.util.common.CheckingParamsUtil;

import java.util.function.Consumer;

public class ClusterSearchParam {
    private String node;
    private String shimName;
    private boolean secured;

    private ClusterSearchParam() {}

    public String getNode() {
        return node;
    }

    public String getShimName() {
        return shimName;
    }

    public boolean isSecured() {
        return secured;
    }

    public static class ClusterSearchParamBuilder {
        private ClusterSearchParam clusterSearchParam;

        private ClusterSearchParamBuilder() {
            this.clusterSearchParam = new ClusterSearchParam();
        }

        public static ClusterSearchParamBuilder get() {
            return new ClusterSearchParamBuilder();
        }

        public ClusterSearchParamBuilder withNode( String node ) {
            setParamWithCheck( node, ( param ) -> this.clusterSearchParam.node = extractHostFromSshLoginString( param ) );

            return this;
        }

        public ClusterSearchParamBuilder withShimName( String clusterType ) {
            setParamWithCheck( clusterType, ( param ) -> this.clusterSearchParam.shimName = param );

            return this;
        }

        public ClusterSearchParamBuilder withSecure( String secured ) {
            setParamWithCheck( secured, ( param ) -> this.clusterSearchParam.secured = Boolean.parseBoolean( param ) );

            return this;
        }

        public ClusterSearchParam build() {
            return this.clusterSearchParam;
        }

        private void setParamWithCheck(String param, Consumer<String> setParamConsumer) {
            if ( !CheckingParamsUtil.isParamsNullOrEmpty( param ) ) {
                setParamConsumer.accept( param );
            }
        }

        private String extractHostFromSshLoginString( String sshLoginString ) {
            return sshLoginString.contains( "@" ) ? sshLoginString.split( "@" )[1] : sshLoginString;
        }
    }
}
