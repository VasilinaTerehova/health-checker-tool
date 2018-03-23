package com.epam.health.tool.transfer.impl;

import com.epam.health.tool.transfer.MappingAcceptor;
import com.epam.health.tool.transfer.SVTransferer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * Created by Vasilina_Terehova on 3/23/2018.
 */
public class ModelMapperTransfererImpl<F, T> implements SVTransferer<F, T> {

    ModelMapper modelMapper = new ModelMapper();

    public ModelMapperTransfererImpl() {
        setup();
    }

    private void setup() {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Override
    public T transfer(F from, Class<T> toClass) {
        return modelMapper.map(from, toClass);
    }

    @Override
    public T transfer(F from, T to) {
        modelMapper.map(from, to);
        return to;
    }

    public void addMappingRule(MappingAcceptor<F, T> mappingAcceptor) {
        modelMapper.addConverter((Converter<F, T>) context -> {
            mappingAcceptor.accept(context.getSource(), context.getDestination());
            return context.getDestination();
        });
    }
}
