package com.epam.health.tool.dao.cluster;

import com.epam.health.tool.model.JobResultEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
@Repository
public interface JobResultDao extends CrudRepository<JobResultEntity, Long> {
}
