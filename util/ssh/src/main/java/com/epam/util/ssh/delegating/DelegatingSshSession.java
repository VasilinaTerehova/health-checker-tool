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

package com.epam.util.ssh.delegating;

import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;
import com.epam.util.common.copier.ByteCopierUtil;
import com.jcraft.jsch.*;

import java.io.*;

public class DelegatingSshSession implements Closeable {
  private Session session;

  public DelegatingSshSession( String user, String host, int port, String password, String identityPath )
    throws IOException {
    session = createSession( user, host, port, password, identityPath );
  }

  public DelegatingSshSession( String user, String host, String password, String identityPath )
          throws IOException {
    session = createSession( user, host, 22, password, identityPath );
  }

  public String downloadFile( String sourcePath ) {
    StringBuilder commandResult = new StringBuilder( StringUtils.EMPTY );
    Channel channel = null;

    try {
      channel = session.openChannel( "sftp" );
      channel.connect();

      SftpATTRS sftpATTRS = ( (ChannelSftp) channel ).stat( sourcePath );
      InputStream in = ( (ChannelSftp) channel ).get( sourcePath );
      int size = sftpATTRS.getSize() > 1024 ? 256 : (int) sftpATTRS.getSize();
      byte[] tmp = new byte[ size ];
      int i;
      while ( ( i = in.read( tmp, 0, size ) ) > 0 ) {
        commandResult.append( new String( tmp, 0, i ) );
      }
    } catch ( JSchException | IOException | SftpException ex ) {
      ex.printStackTrace();
    } finally {
      if ( channel != null ) {
        channel.disconnect();
      }
    }

    return commandResult.toString();
  }

  public byte[] downloadFileAsByteArray( String sourcePath ) {
    byte[] commandResult = null;
    Channel channel = null;

    try {
      channel = session.openChannel( "sftp" );
      channel.connect();

      SftpATTRS sftpATTRS = ( (ChannelSftp) channel ).stat( sourcePath );
      InputStream in = ( (ChannelSftp) channel ).get( sourcePath );
      int size = sftpATTRS.getSize() > 1024 ? 256 : (int) sftpATTRS.getSize();
      byte[] tmp = new byte[ size ];
      int i;
      while ( ( i = in.read( tmp, 0, size ) ) > 0 ) {
        commandResult = ByteCopierUtil.addBytesToArray( commandResult, tmp, i );
      }
    } catch ( JSchException | IOException | SftpException ex ) {
      ex.printStackTrace();
    } finally {
      if ( channel != null ) {
        channel.disconnect();
      }
    }

    return commandResult;
  }

  public SshExecResult executeCommand( String command ) {
    SshExecResult.SshExecResultBuilder sshExecResultBuilder = SshExecResult.SshExecResultBuilder.get();
    Channel channel = null;

    try {
      channel = session.openChannel( "exec" );
      setPtyIfSudo( channel, command );
      ( (ChannelExec) channel ).setCommand( command.trim() );

      channel.setInputStream( null );
      ( (ChannelExec) channel ).setErrStream( System.err );
      InputStream in = channel.getInputStream();
      InputStream err = ((ChannelExec) channel).getErrStream();
      channel.connect();
      byte[] tmp = new byte[ 10024 ];
      while ( true ) {
        while ( in.available() > 0 ) {
          int i = in.read( tmp, 0, 5024 );
          if ( i < 0 ) {
            break;
          }
          sshExecResultBuilder.appendToOut( new String( tmp, 0, i ) );
        }
        while ( err.available() > 0 ) {
          int i = err.read( tmp, 0, 5024 );
          if ( i < 0 ) {
            break;
          }
          sshExecResultBuilder.appendToErr( new String( tmp, 0, i ) );
        }
        if ( channel.isClosed() ) {
          if ( in.available() > 0 || err.available() > 0 ) {
            continue;
          }
          break;
        }
        try {
          Thread.sleep( 100 );
        } catch ( Exception ex ) {
          System.out.println( ex.getMessage() );
        }
      }
    } catch ( JSchException | IOException ex ) {
      ex.printStackTrace();
    } finally {
      if ( channel != null ) {
        channel.disconnect();
      }
    }

    return sshExecResultBuilder.build();
  }

  @Override
  public void close() throws IOException {
    session.disconnect();
  }

  private Session createSession( String user, String host, int port, String password, String identityPath )
    throws IOException {
    try {
      JSch jsch = new JSch();
      if ( identityPath != null && !identityPath.isEmpty() ) {
        jsch.addIdentity( identityPath );
      }

      Session session = jsch.getSession( user, host, port );
      session.setConfig( "StrictHostKeyChecking", "no" );
      session.setConfig( "GSSAPIAuthentication", "yes" );
      session.setConfig( "GSSAPIDelegateCredentials", "no" );
      session.setConfig( "UseDNS", "no" );
      if ( identityPath == null || identityPath.isEmpty() ) {
        session.setPassword( password );
      }

      session.connect();

      return session;
    } catch ( JSchException ex ) {
      throw new IOException( ex );
    }
  }

  private void setPtyIfSudo( Channel channel, String command ) {
    if (CheckingParamsUtil.isParamsNotNullOrEmpty( command ) && command.contains( "sudo" )) {
      ( (ChannelExec) channel ).setPty( true );
    }

  }
}
