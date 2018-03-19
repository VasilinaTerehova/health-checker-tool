package com.epam.health.tool.controller.cluster;

import com.epam.health.tool.facade.cluster.IClusterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClusterController {
    @Autowired
    private IClusterFacade clusterFacade;

    @RequestMapping("/clusters")
    public List<Object> getClusterList() {
        return clusterFacade.getClusterList();
    }
}
