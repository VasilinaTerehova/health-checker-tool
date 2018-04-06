package com.epam.health.tool.facade.common.service.action;

import java.util.Arrays;
import java.util.List;

public class CommonActionNames {
    public final static String YARN_EXAMPLES = "YARN_EXAMPLES";
    public final static String DFS_CHECK = "DFS_CHECK";

    public static List<String> getNames() {
        return Arrays.asList( YARN_EXAMPLES, DFS_CHECK );
    }
}
