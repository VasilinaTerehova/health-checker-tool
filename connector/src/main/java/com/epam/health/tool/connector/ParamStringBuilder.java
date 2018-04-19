package com.epam.health.tool.connector;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ParamStringBuilder {
    private ParamStringBuilder() {}

    public static ParamStringBuilder get() {
        return new ParamStringBuilder();
    }

    public String buildFromSearchParams( ClusterSearchParam clusterSearchParam ) {
        return Arrays.stream( createParamExpressionsArray( clusterSearchParam ) ).collect(Collectors.joining( "&" ));
    }

    private String[] createParamExpressionsArray( ClusterSearchParam clusterSearchParam ) {
        return new String[]{
                    createParamExpression( "node", clusterSearchParam.getNode() ),
                    createParamExpression( "shimName", clusterSearchParam.getShimName() ),
                    createParamExpression( "secured", clusterSearchParam.isSecured() )
                };
    }

    private String createParamExpression( String name, String value ) {
        return name.concat( "=" ).concat( value );
    }

    private String createParamExpression( String name, boolean value ) {
        return createParamExpression( name, String.valueOf( value ) );
    }
}
