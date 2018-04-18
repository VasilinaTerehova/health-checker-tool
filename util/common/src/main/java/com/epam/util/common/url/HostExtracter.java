package com.epam.util.common.url;

import java.util.Arrays;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public class HostExtracter {
    public static String[] getNodePart(String fullName) {
        return new String[] {fullName.substring(0, fullName.indexOf(".")), fullName.substring(fullName.indexOf("."))};
    }

    public static String[] getAllNodeNames(String fullName, int countOfNodes) {
        String[] firstNode = getNodePart(fullName);
        String nodeName = firstNode[0];
        String dmzPart = firstNode[1];
        String nodeNameNoNumber = nodeName.substring(0, nodeName.length() - 1);
        String[] nodeNames = new String[countOfNodes];
        for (int i =0 ; i<countOfNodes; i++) {
            nodeNames[i] = nodeNameNoNumber + (i+1) + dmzPart;
        }
        return nodeNames;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getAllNodeNames("svqxbdcn6hdp26secn1.pentahoqa.com", 3)));
    }
}
