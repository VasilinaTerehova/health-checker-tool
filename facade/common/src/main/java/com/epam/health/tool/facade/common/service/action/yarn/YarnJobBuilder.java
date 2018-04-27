package com.epam.health.tool.facade.common.service.action.yarn;

import com.epam.facade.model.accumulator.results.impl.JobResultImpl;
import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.util.common.CheckingParamsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YarnJobBuilder {
    private String name;
    private boolean success;
    private List<String> errors;

    private YarnJobBuilder() {
        this.errors = new ArrayList<>();
    }

    public static YarnJobBuilder get() {
        return new YarnJobBuilder();
    }

    public YarnJobBuilder withName(String name) {
        this.name = name;

        return this;
    }

    public YarnJobBuilder withSuccess(boolean success) {
        this.success = success;

        return this;
    }

    public YarnJobBuilder withErrors(String... errors) {
        this.errors.addAll(Arrays.asList(errors));

        return this;
    }

    public JobResultProjection build() throws InvalidBuildParamsException {
        assertParams();

        return new JobResultImpl(name, success, errors);
    }

    private void assertParams() throws InvalidBuildParamsException {
        if (CheckingParamsUtil.isParamsNullOrEmpty(name)) {
            throw new InvalidBuildParamsException("Name must be not null or empty!");
        }
    }
}
