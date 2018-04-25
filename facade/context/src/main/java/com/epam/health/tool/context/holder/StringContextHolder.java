package com.epam.health.tool.context.holder;

import com.epam.health.tool.context.BaseContextHolder;

public class StringContextHolder extends BaseContextHolder<String> {
    public StringContextHolder(String saved) {
        super(saved);
    }

    @Override
    public Class<?> getObjectClass() {
        return String.class;
    }
}
