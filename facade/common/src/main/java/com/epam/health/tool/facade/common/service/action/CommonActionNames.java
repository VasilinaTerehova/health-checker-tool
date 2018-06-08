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

package com.epam.health.tool.facade.common.service.action;

import java.util.Arrays;
import java.util.List;

public class CommonActionNames {
    public final static String YARN_EXAMPLES = "YARN_EXAMPLES";
    public final static String HDFS_CHECK = "DFS_CHECK";
    public final static String MEMORY_CHECK = "MEMORY_CHECK";
    public final static String FS_CHECK = "FS_CHECK";
    public final static String HDFS_TOTAL_CHECK = "HDFS_TOTAL_CHECK";

    public static List<String> getNames() {
        return Arrays.asList( YARN_EXAMPLES, HDFS_CHECK , MEMORY_CHECK, FS_CHECK, HDFS_TOTAL_CHECK);
    }
}
