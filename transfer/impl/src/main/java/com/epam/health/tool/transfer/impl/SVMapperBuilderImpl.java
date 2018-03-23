package com.epam.health.tool.transfer.impl;

import com.epam.health.tool.transfer.MappingAcceptor;
import com.epam.health.tool.transfer.SVMapperBuilder;
import com.epam.health.tool.transfer.SVTransferer;

/**
 * Created by Vasilina_Terehova on 3/23/2018.
 */
public class SVMapperBuilderImpl<F, T> implements SVMapperBuilder<F, T> {
    ModelMapperTransfererImpl<F, T> ftModelMapperTransferer;

    public SVMapperBuilderImpl() {
        ftModelMapperTransferer = new ModelMapperTransfererImpl<>();
    }

    @Override
    public void addMappingRule(MappingAcceptor<F, T> mappingAcceptor) {
        ftModelMapperTransferer.addMappingRule(mappingAcceptor);
    }

    @Override
    public SVTransferer<F, T> build() {
        return ftModelMapperTransferer;
    }
}
