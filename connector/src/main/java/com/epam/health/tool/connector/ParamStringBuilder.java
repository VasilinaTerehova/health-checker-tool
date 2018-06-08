/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

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
