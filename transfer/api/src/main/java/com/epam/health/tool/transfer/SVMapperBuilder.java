package com.epam.health.tool.transfer;

/**
 * Created by Vasilina_Terehova on 3/23/2018.
 */
public interface SVMapperBuilder<F, T> {

    void addMappingRule(MappingAcceptor<F, T> mappingAcceptor);

    SVTransferer<F, T> build();
}
