package com.epam.facade.model;

import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.util.common.CheckingParamsUtil;

import java.util.function.Consumer;

//Will be implemented
public abstract class BaseObjectBuilder<T> {
    protected T object;

    public T build() throws InvalidBuildParamsException {
        this.assertParamsSetup();

        return this.object;
    }

    protected abstract void assertParamsSetup() throws InvalidBuildParamsException;

    protected BaseObjectBuilder<T> addStringParam( String param, Consumer<String> addParamAction ) {
        addStringParamWithCheck( param, addParamAction );

        return this;
    }

    private void addStringParamWithCheck( String param, Consumer<String> addParamAction ) {
        if ( CheckingParamsUtil.isParamsNotNullOrEmpty( param ) ) {
            addParamAction.accept( param );
        }
    }
}
