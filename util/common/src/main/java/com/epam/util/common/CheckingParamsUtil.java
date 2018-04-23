package com.epam.util.common;

import java.util.Arrays;
import java.util.Objects;

public class CheckingParamsUtil {
  public static boolean isParamsNullOrEmpty( String... params ) {
    return !(params != null && Arrays.stream( params ).allMatch( param -> Objects.nonNull( param ) && !param.isEmpty() ));
  }

  public static boolean isParamsNotNullOrEmpty( String... params ) {
    return params != null && Arrays.stream( params ).allMatch( param -> Objects.nonNull( param ) && !param.isEmpty() );
  }

  public static boolean isParamsNull( String... params ) {
    return !(params != null && Arrays.stream( params ).allMatch( Objects::nonNull ));
  }
}
