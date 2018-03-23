package com.epam.health.tool.transfer;

/**
 * Created by Vasilina_Terehova on 3/22/2018.
 */
public interface SVTransferer<F, T> {
    T transfer(F from, Class<T> toClass);
    T transfer(F from, T to);
}
