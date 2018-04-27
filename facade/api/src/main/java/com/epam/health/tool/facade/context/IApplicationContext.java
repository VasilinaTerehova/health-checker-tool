package com.epam.health.tool.facade.context;

import com.epam.facade.model.context.ContextKey;

public interface IApplicationContext {
    void putHolder( ContextKey holderKey, ISingleContextHolder holder );
    ISingleContextHolder removeHolder( ContextKey holderKey );
    <T> ISingleContextHolder<T> getHolder( ContextKey holderKey );
    //Get value from specific holder if exists or return default
    <T> T getFromContext( String clusterName, String minorKey, Class<?> holderClass, T defaultValue );
    void addToContext( String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value );
    //Remove all holders by specific minor key
    void removeAllByMinorKey( String minorKey );
}
