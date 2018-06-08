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

package com.epam.util.common.copier;

public class ByteCopierUtil {
  public static byte[] addBytesToArray( byte[] dest, byte[] addition, int additionCount ) {
    dest = normalizeArray( dest );
    addition = normalizeArray( addition );
    additionCount = normalizeAdditionCount( addition.length, additionCount );

    byte[] result = new byte[ dest.length + additionCount ];

    System.arraycopy( dest, 0, result, 0, dest.length );
    System.arraycopy( addition, 0, result, dest.length, additionCount );

    return result;
  }

  private static int normalizeAdditionCount( int additionArrayLength, int additionCount ) {
    return additionCount < 1 || additionCount > additionArrayLength ? additionArrayLength : additionCount;
  }

  private static byte[] normalizeArray( byte[] array ) {
    return array == null ? new byte[0] : array;
  }
}
