package com.epam.health.tool.facade.context;

import com.epam.facade.model.context.ContextKey;

public interface IApplicationContext {
    void putHolder( ContextKey holderKey, ISingleContextHolder holder );
    ISingleContextHolder removeHolder( ContextKey holderKey );
    <T> ISingleContextHolder<T> getHolder( ContextKey holderKey );
    <T> T getFromContext( String clusterName, String minorKey, Class<?> holderClass, T defaultValue );
    void addToContext( String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value );
    void removeAllByMinorKey( String minorKey );
}
