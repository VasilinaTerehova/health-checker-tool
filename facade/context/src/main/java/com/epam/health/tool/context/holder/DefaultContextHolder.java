package com.epam.health.tool.context.holder;

import com.epam.health.tool.context.BaseContextHolder;

public class DefaultContextHolder extends BaseContextHolder<Object> {
    public DefaultContextHolder() { }

    @Override
    public Class<?> getObjectClass() {
        return null;
    }
}
