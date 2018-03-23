package com.epam.health.tool.transfer;

/**
 * Created by Vasilina_Terehova on 3/23/2018.
 */
public interface MappingAcceptor<F, T> {
    T accept(F from, T to);
}
