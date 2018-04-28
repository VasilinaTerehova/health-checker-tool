package com.epam.facade.model.accumulator.results.impl;

import com.epam.facade.model.projection.JobResultProjection;
import com.epam.util.common.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/24/2018.
 */
public class JobResultImpl implements JobResultProjection {
    private String name;
    private boolean success;
    private List<String> alerts;

    public JobResultImpl() {
        this( StringUtils.EMPTY, false, Collections.emptyList() );
    }

    public JobResultImpl(String name) {
        this( name, false, Collections.emptyList() );
    }

    public JobResultImpl(String name, boolean success, List<String> alerts) {
        this.name = name;
        this.success = success;
        this.alerts = alerts;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public List<String> getAlerts() {
        return alerts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }
}
