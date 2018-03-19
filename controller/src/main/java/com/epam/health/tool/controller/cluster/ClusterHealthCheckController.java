package com.epam.health.tool.controller.cluster;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterHealthCheckController {
    @RequestMapping( "/getClusterStatus" )
    public Object getClusterStatus( @RequestParam( "clusterName" ) String clusterName ) {
        return null;
    }
}
