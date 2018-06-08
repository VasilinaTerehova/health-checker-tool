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

package com.epam.util.common.file;

import com.epam.util.common.copier.ByteCopierUtil;

public class DownloadedFileWrapper {
  private byte[] byteFileContent;
  private String stringFileContent;

  public DownloadedFileWrapper(byte[] byteFileContent ) {
    this.byteFileContent = ByteCopierUtil.addBytesToArray( null, byteFileContent, byteFileContent.length );
  }

  public DownloadedFileWrapper(String stringFileContent ) {
    this.stringFileContent = stringFileContent;
  }

  public void setByteFileContent( byte[] byteFileContent ) {
    this.byteFileContent = ByteCopierUtil.addBytesToArray( null, byteFileContent, byteFileContent.length );
  }

  public void setStringFileContent( String stringFileContent ) {
    this.stringFileContent = stringFileContent;
  }

  public byte[] getByteFileContent() {
    return byteFileContent;
  }

  public String getStringFileContent() {
    return stringFileContent;
  }

  public boolean isEmpty() {
    return isByteContentEmpty() && isStringContentEmpty();
  }

  public boolean isByteContentEmpty() {
    return byteFileContent == null || byteFileContent.length < 1;
  }

  public boolean isStringContentEmpty() {
    return stringFileContent == null || stringFileContent.isEmpty();
  }
}
