package com.epam.health.tool.facade.common.service.fix.action;

import com.epam.facade.model.INoArgConsumer;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.StringUtils;
import com.epam.util.common.file.FileCommonUtil;

import java.io.File;
import java.util.Date;

public class BashScriptCreator {
    private final static String BASH_HEADER = "#!/bin/bash";
    private final static String UPLOAD_DIR_PREFIX = "upload";
    private String commands;
    private String clusterName;
    private String serviceName;

    private BashScriptCreator( String commands ) {
        this.commands = commands;
        this.clusterName = "NO_NAME";
        this.serviceName = "NO_NAME";
    }

    public static BashScriptCreator get( String commands ) {
        return new BashScriptCreator( commands );
    }

    public BashScriptCreator withCluster( String clusterName ) {
        return verifyAndAddStringParam( clusterName, () -> this.clusterName = clusterName );
    }

    public BashScriptCreator withService( String serviceName ) {
        return verifyAndAddStringParam( serviceName, () -> this.serviceName = serviceName );
    }

    public String generateScript() {
        return BASH_HEADER.concat( "\n" ).concat( commands );
    }

    public String generateAndSaveScript() {
        try {
            String pathToLoad = generatePathToLoad();
            FileCommonUtil.writeStringToFile( generatePathToSave( pathToLoad ), generateScript() );

            return pathToLoad;
        } catch (CommonUtilException | RuntimeException e) {
            return StringUtils.EMPTY;
        }
    }

    private String generatePathToLoad() {
        return "scripts".concat( File.separator ).concat( this.clusterName )
                .concat( File.separator ).concat( this.serviceName ).concat( "_" )
                .concat( String.valueOf( new Date().getTime() ) ).concat( ".sh" );
    }

    private BashScriptCreator verifyAndAddStringParam( String param, INoArgConsumer noArgConsumer ) {
        if ( CheckingParamsUtil.isParamsNotNullOrEmpty( param ) ) {
            noArgConsumer.execute();
        }

        return this;
    }

    private String generatePathToSave( String pathToLoad ) {
        return getTmpDir().concat( File.separator ).concat( pathToLoad );
    }

    private String getTmpDir() {
        return System.getProperty( "java.io.tmpdir", StringUtils.EMPTY ).concat( UPLOAD_DIR_PREFIX );
    }
}
