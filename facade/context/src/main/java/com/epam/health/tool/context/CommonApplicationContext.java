package com.epam.health.tool.context;

import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.facade.model.context.ContextKey;
import com.epam.health.tool.context.holder.DefaultContextHolder;
import com.epam.health.tool.facade.context.IApplicationContext;
import com.epam.health.tool.facade.context.ISingleContextHolder;
import com.epam.util.common.CheckingParamsUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Should use sync methods
@Component("singleton")
public class CommonApplicationContext implements IApplicationContext {
    private Map<ContextKey, ISingleContextHolder> singleContextHolderMap;

    public CommonApplicationContext() {
        this.singleContextHolderMap = new HashMap<>();
    }

    @Override
    public void putHolder( ContextKey holderKey, ISingleContextHolder holder ) {
        singleContextHolderMap.put( holderKey, holder );
    }

    @Override
    public void putHolderIfAbsent(ContextKey holderKey, ISingleContextHolder holder) {
        singleContextHolderMap.putIfAbsent( holderKey, holder );
    }

    @Override
    public ISingleContextHolder removeHolder( ContextKey holderKey ) {
        return singleContextHolderMap.remove( holderKey );
    }

    @Override
    public <T> ISingleContextHolder<T> getHolder( ContextKey holderKey ) {
        return singleContextHolderMap.getOrDefault( holderKey, new DefaultContextHolder());
    }

    public <T> T getFromContext( String clusterName, String minorKey, Class<?> holderClass, T defaultValue ) {
        try {
            return this.<T>getHolder( buildContextKey( clusterName, minorKey, holderClass ) ).orElse( defaultValue );
        } catch (InvalidBuildParamsException e) {
//            logger.error( e.getMessage() );
            return defaultValue;
        }
    }

    public void addToContext( String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value ) {
        try {
            this.putHolder( buildContextKey( clusterName, minorKey, holderClass ), value );
        } catch (InvalidBuildParamsException e) {
//            logger.error( e.getMessage() );
        }
    }

    @Override
    public void addToContextIfAbsent(String clusterName, String minorKey, Class<?> holderClass, ISingleContextHolder value) {
        try {
            this.putHolderIfAbsent( buildContextKey( clusterName, minorKey, holderClass ), value );
        } catch (InvalidBuildParamsException e) {
//            logger.error( e.getMessage() );
        }
    }

    public void removeAllByMinorKey(String minorKey ) {
        List<ContextKey> keys = singleContextHolderMap.entrySet().stream().map(Map.Entry::getKey).filter(key -> isKeysEquals( key.getMinorKey(), minorKey ) )
                .collect(Collectors.toList());

        keys.forEach( singleContextHolderMap::remove );
    }

    private boolean isKeysEquals( String firstKey, String secondKey ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( firstKey, secondKey ) && firstKey.equals( secondKey );
    }

    private ContextKey buildContextKey(String clusterName, String minorKey, Class<?> holderClass ) throws InvalidBuildParamsException {
        return ContextKey.ContextKeyBuilder.get().withMajorKey( clusterName )
                .withMinorKey( minorKey ).withHolderClass( holderClass ).build();
    }
}
