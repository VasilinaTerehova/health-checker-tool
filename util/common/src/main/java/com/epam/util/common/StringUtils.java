package com.epam.util.common;

public class StringUtils {
    public static String EMPTY = "";

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
