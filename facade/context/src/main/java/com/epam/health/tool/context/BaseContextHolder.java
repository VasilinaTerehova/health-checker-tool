package com.epam.health.tool.context;

import com.epam.health.tool.facade.context.ISingleContextHolder;

public abstract class BaseContextHolder<T> implements ISingleContextHolder<T> {
    protected T saved;

    public BaseContextHolder() {}

    public BaseContextHolder(T saved) {
        this.saved = saved;
    }

    @Override
    public void save(T object) {
        this.saved = object;
    }

    @Override
    public T get() {
        return this.saved;
    }

    @Override
    public T merge(T object) {
        return this.saved;
    }
}
