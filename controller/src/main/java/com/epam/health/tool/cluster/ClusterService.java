package com.epam.health.tool.cluster;

import com.epam.health.tool.model.Cluster;
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
