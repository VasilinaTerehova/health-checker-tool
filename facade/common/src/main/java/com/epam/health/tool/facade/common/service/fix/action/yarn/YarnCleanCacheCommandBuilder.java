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

package com.epam.health.tool.facade.common.service.fix.action.yarn;

import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class YarnCleanCacheCommandBuilder {
    private static final String SUDO_RM_FOLDER = "sudo rm -r";
    private static final String SUDO_MKDIR = "sudo mkdir";
    private static final String SUDO_CHOWN_YARN = "sudo chown yarn:yarn";
    private static final String USER_CACHE = "usercache";
    private static final String FILE_CACHE = "filecache";
    private static final String COMMAND_SEPARATOR = " && ";

    private YarnCleanCacheCommandBuilder() {}

    public static YarnCleanCacheCommandBuilder get() {
        return new YarnCleanCacheCommandBuilder();
    }

    public String buildCommand( String yarnLocalDirsPath ) {
        return Arrays.stream( createCommandArray( yarnLocalDirsPath ) )
                .filter( CheckingParamsUtil::isParamsNotNullOrEmpty )
                .collect( Collectors.joining( COMMAND_SEPARATOR ) );
    }

    public List<String> buildStepList( String yarnLocalDirsPath ) {
        return Arrays.stream( createCommandArray( yarnLocalDirsPath ) )
                .filter( CheckingParamsUtil::isParamsNotNullOrEmpty )
                .collect( Collectors.toList() );
    }

    public boolean verifyCommand( String command ) {
        return command.contains( COMMAND_SEPARATOR ) && command.split( COMMAND_SEPARATOR ).length == getCommandsCount();
    }

    private int getCommandsCount() {
        return createCommandArray( "not_empty" ).length;
    }

    private String[] createCommandArray( String yarnLocalDirsPath ) {
        return new String[] {
                createRmLocalDirsCommand( yarnLocalDirsPath, USER_CACHE ),
                createRmLocalDirsCommand( yarnLocalDirsPath, FILE_CACHE ),
                createMkdirLocalDirsCommand( yarnLocalDirsPath, USER_CACHE ),
                createMkdirLocalDirsCommand( yarnLocalDirsPath, FILE_CACHE ),
                createChownLocalDirsCommand( yarnLocalDirsPath, USER_CACHE ),
                createChownLocalDirsCommand( yarnLocalDirsPath, FILE_CACHE )
        };
    }

    private String createRmLocalDirsCommand( String yarnLocalDirsPath, String additionalPath ) {
        return createCommand( SUDO_RM_FOLDER, yarnLocalDirsPath, additionalPath );
    }

    private String createMkdirLocalDirsCommand( String yarnLocalDirsPath, String additionalPath ) {
        return createCommand( SUDO_MKDIR, yarnLocalDirsPath, additionalPath );
    }

    private String createChownLocalDirsCommand( String yarnLocalDirsPath, String additionalPath ) {
        return createCommand( SUDO_CHOWN_YARN, yarnLocalDirsPath, additionalPath );
    }

    private String createCommand( String command, String yarnLocalDirsPath, String additionalPath ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( command, yarnLocalDirsPath, additionalPath )
                ? command.concat( " " ).concat( yarnLocalDirsPath ).concat( "/" ).concat( additionalPath )
                : StringUtils.EMPTY;
    }
}
