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

package com.epam.facade.model.validation;

import com.epam.util.common.StringUtils;

public class ClusterHealthValidationResult {
    private boolean clusterHealthy;
    private String errorSummary;

    public ClusterHealthValidationResult() { }

    public ClusterHealthValidationResult(boolean clusterHealthy) {
        this.clusterHealthy = clusterHealthy;
        this.errorSummary = StringUtils.EMPTY;
    }

    public ClusterHealthValidationResult(boolean clusterHealthy, String errorSummary) {
        this.clusterHealthy = clusterHealthy;
        this.errorSummary = errorSummary;
    }

    public boolean isClusterHealthy() {
        return clusterHealthy;
    }

    public void setClusterHealthy(boolean clusterHealthy) {
        this.clusterHealthy = clusterHealthy;
    }

    public String getErrorSummary() {
        return errorSummary;
    }

    public void setErrorSummary(String errorSummary) {
        this.errorSummary = errorSummary;
    }

    public void appendErrorSummary( String error ) {
        if ( this.errorSummary != null ) {
            this.errorSummary = this.errorSummary.concat( "\n" ).concat( error );
        }
        else {
            this.setErrorSummary( error );
        }
    }
}
