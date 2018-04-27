package com.epam.health.tool.context.holder;

import com.epam.health.tool.context.BaseContextHolder;

import javax.security.auth.Subject;

public class SubjectContextHolder extends BaseContextHolder<Subject> {
    public SubjectContextHolder(Subject saved) {
        super(saved);
    }

    @Override
    public Class<?> getObjectClass() {
        return Subject.class;
    }
}
