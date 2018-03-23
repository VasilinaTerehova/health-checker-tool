package com.epam.health.tool.transfer.impl;

import com.epam.health.tool.transfer.SVTransferer;
import com.epam.health.tool.transfer.Tuple2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vasilina_Terehova on 3/23/2018.
 */
@Component
public class SVTransfererManager {

    ConcurrentHashMap<Tuple2<Class, Class>, SVTransferer> transferersMap = new ConcurrentHashMap<>();

    public void addTransferer(Class fromClass, Class toClass, SVTransferer transferer) {
        transferersMap.put(new Tuple2<>(fromClass, toClass), transferer);
    }

    public <F, T> SVTransferer<F, T> getTransferer(Class fromClass, Class toClass) {
        Tuple2<Class, Class> classTuple = new Tuple2<>(fromClass, toClass);
        SVTransferer svTransferer = transferersMap.get(classTuple);
        if (svTransferer == null) {
            svTransferer = new SVMapperBuilderImpl<F, T>().build();
            addTransferer(fromClass, toClass, svTransferer);
        }
        return svTransferer;
    }
}
