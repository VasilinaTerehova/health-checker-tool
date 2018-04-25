package com.epam.health.tool.facade.context;

public interface ISingleContextHolder<T> {
    void save( T object );
    T get();
    T merge( T object );
    Class<?> getObjectClass();
}
