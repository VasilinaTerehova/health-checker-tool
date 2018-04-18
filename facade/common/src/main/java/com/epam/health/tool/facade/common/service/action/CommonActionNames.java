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
