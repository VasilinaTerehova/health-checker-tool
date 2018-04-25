package com.epam.health.tool.context.holder;

import com.epam.health.tool.context.BaseContextHolder;

import java.util.Set;

public class NodesContextHolder extends BaseContextHolder<Set<String>> {
    public NodesContextHolder(Set<String> saved) {
        super(saved);
    }

    @Override
    public Class<?> getObjectClass() {
        return Set.class;
    }
}
