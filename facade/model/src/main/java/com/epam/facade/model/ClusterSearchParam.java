package com.epam.facade.model;

import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CheckingParamsUtil;

import java.util.function.Consumer;

public class ClusterSearchParam {
    private String node;
    private ClusterTypeEnum clusterType;
    private boolean secured;

    private ClusterSearchParam() {}

    public String getNode() {
        return node;
    }

    public ClusterTypeEnum getClusterType() {
        return clusterType;
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
            setParamWithCheck( node, ( param ) -> this.clusterSearchParam.node = param );

            return this;
        }

        public ClusterSearchParamBuilder withClusterType( String clusterType ) {
            setParamWithCheck( clusterType, ( param ) -> this.clusterSearchParam.clusterType = ClusterTypeEnum.valueOf( param.replaceAll("\\d", "").toUpperCase() ) );

            return this;
        }

        public ClusterSearchParamBuilder withSecure( boolean secured ) {
            this.clusterSearchParam.secured = secured;

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
    }
}
