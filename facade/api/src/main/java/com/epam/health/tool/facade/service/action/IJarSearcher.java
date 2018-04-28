package com.epam.health.tool.facade.service.action;


public interface IJarSearcher {
    String searchJarPath(String jarMask, String clusterName, String possiblePath);
    // Shows performance speed rating 0 - very fast, will be used firstly
    int speedRating();
}
