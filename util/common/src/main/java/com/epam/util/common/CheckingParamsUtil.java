/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.util.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class CheckingParamsUtil {
  public static boolean isParamsNullOrEmpty( String... params ) {
    return !isParamsNotNullOrEmpty( params );
  }

  public static boolean isParamsNotNullOrEmpty( String... params ) {
    return params != null && Arrays.stream( params ).allMatch( CheckingParamsUtil::isParamNotEmpty );
  }

  public static boolean isParamsNull( String... params ) {
    return !(params != null && Arrays.stream( params ).allMatch( Objects::nonNull ));
  }

  public static boolean isParamNotEmpty( String param ) {
    return Objects.nonNull( param ) && !param.isEmpty();
  }

  public static boolean isParamNotNull( String param ) {
    return Objects.nonNull( param );
  }

  public static boolean isParamListNotNullOrEmpty( Collection<?> collection ) {
    return collection != null && !collection.isEmpty();
  }

  public static boolean isParamListNotNullOrEmptyAndNoNullValues( Collection<?> collection ) {
    return isParamListNotNullOrEmpty( collection ) && collection.stream().allMatch( Objects::nonNull );
  }
}
