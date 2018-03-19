package com.epam.health_tool.cluster;

import com.epam.health_tool.model.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClusterService {
    @Autowired
    private ClusterRepository clusterRepository;

    public List<Cluster> getClustersList() {
        return clusterRepository.findClustersList();
    }
}
