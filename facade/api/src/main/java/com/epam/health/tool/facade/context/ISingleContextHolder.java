package com.epam.health.tool.facade.context;

import java.util.Objects;

public interface ISingleContextHolder<T> {
    void save( T object );
    T get();
    T merge( T object );
    default T orElse( T object ) {
        T temp = get();

        return Objects.nonNull( temp ) ? temp : object;
    }
    Class<?> getObjectClass();
}
