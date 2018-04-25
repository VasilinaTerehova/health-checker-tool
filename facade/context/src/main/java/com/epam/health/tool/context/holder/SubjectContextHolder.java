package com.epam.health.tool.context.holder;

import com.epam.health.tool.facade.context.ISingleContextHolder;

import javax.security.auth.Subject;

public class SubjectContextHolder implements ISingleContextHolder<Subject> {
    private Subject subject;

    public SubjectContextHolder(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void save(Subject object) {
        this.subject = object;
    }

    @Override
    public Subject get() {
        return subject;
    }

    @Override
    public Subject merge(Subject object) {
        return subject;
    }

    @Override
    public Class<?> getObjectClass() {
        return Subject.class;
    }
}
