package com.epam.health.tool.facade.common.service;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by Vasilina_Terehova on 4/5/2018.
 */
public class ReflectionToString {
    public static void main(String[] args) {
        Class<?> c = MemoryMetricsJson.class;

        Field[] chap = c.getDeclaredFields();
        final String[] fieldsSummary = {""};
        Arrays.stream(chap).forEach(field -> fieldsSummary[0] += "\" " + field.getName() + ": \"+ " + field.getName() + " + \" \" + " );
        System.out.println(fieldsSummary[0]);

    }
}
