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

import com.epam.util.common.CommonUtilException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileCommonUtil {
  private static Logger logger = Logger.getLogger( FileCommonUtil.class );

  public static void writeByteArrayToFile( String dest, byte[] input ) throws CommonUtilException {
    try {
      FileUtils.writeByteArrayToFile( new File( dest ), input );
    } catch ( IOException e ) {
      throw new CommonUtilException( e );
    }
  }

  public static void writeStringToFile( String dest, String input ) throws CommonUtilException {
    try {
      FileUtils.writeStringToFile( new File( dest ), input );

      logger.info( "Save - " + dest );
    } catch ( IOException e ) {
      throw new CommonUtilException( e );
    }
  }

  public static void extractFilesFromTarArchiveByteArray( byte[] source, List<String> unpackingFileNames,
                                                          String destPrefix ) throws CommonUtilException {
    try ( TarArchiveInputStream debInputStream =
            new TarArchiveInputStream( new GzipCompressorInputStream(
              new ByteArrayInputStream( source ) ) ) ) {
      TarArchiveEntry entry = null;
      while ( ( entry = debInputStream.getNextTarEntry() ) != null ) {
        saveEntry( entry, debInputStream, unpackingFileNames, destPrefix );
      }
    } catch ( IOException ex ) {
      throw new CommonUtilException( ex );
    }
  }

  public static void extractFilesFromZipArchiveByteArray( byte[] source, List<String> unpackingFileNames,
                                                          String destPrefix ) throws CommonUtilException {
    try ( ZipArchiveInputStream debInputStream =
            new ZipArchiveInputStream(
              new ByteArrayInputStream( source ) ) ) {
      ZipArchiveEntry entry = null;
      while ( ( entry = debInputStream.getNextZipEntry() ) != null ) {
        saveEntry( entry, debInputStream, unpackingFileNames, destPrefix );
      }
    } catch ( IOException ex ) {
      throw new CommonUtilException( ex );
    }
  }

  private static void saveEntry( ArchiveEntry entry, ArchiveInputStream archiveInputStream,
                                 List<String> unpackingFileNames, String destPrefix ) throws CommonUtilException {
    try ( ByteArrayOutputStream out = new ByteArrayOutputStream() ) {
      if ( !entry.isDirectory() ) {
        List<String> matchingFileNames =
          unpackingFileNames.stream().filter( fileName -> entry.getName().contains( fileName ) )
            .collect( Collectors.toList() );
        if ( !matchingFileNames.isEmpty() ) {
          byte[] buffer = new byte[ 15000 ];
          int len;
          while ( ( len = archiveInputStream.read( buffer ) ) != -1 ) {
            out.write( buffer, 0, len );
          }
          writeStringToFile( destPrefix + File.separator + matchingFileNames.get( 0 ), out.toString() );
        }
      }
    } catch ( IOException ex ) {
      throw new CommonUtilException( ex );
    }
  }

  public static void deleteCommentsFromXmlFile( String pathToFile ) throws CommonUtilException {
    try {
      String modifiedFile = Files.lines( Paths.get( pathToFile ) ).filter( line -> line.startsWith( "<!-" ) )
        .collect( Collectors.joining( "\n" ) );
      FileCommonUtil.writeStringToFile( pathToFile, modifiedFile );
    } catch ( IOException e ) {
      throw new CommonUtilException( e );
    }
  }
}
