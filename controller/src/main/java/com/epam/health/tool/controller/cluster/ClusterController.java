package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClusterController {
    @Autowired
    private IClusterFacade clusterFacade;

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping("/clusters")
    public List<ClusterEntityProjection> getClusterList() {
        return clusterFacade.getClusterList();
    }
}
