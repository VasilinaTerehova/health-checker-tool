package com.epam.health.tool.controller.cluster;

import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClusterController {
    @Autowired
    private IClusterFacade clusterFacade;

    @GetMapping("/clusters")
    public List<ClusterEntity> getClusterList() {
        return clusterFacade.getClusterList();
    }
}
