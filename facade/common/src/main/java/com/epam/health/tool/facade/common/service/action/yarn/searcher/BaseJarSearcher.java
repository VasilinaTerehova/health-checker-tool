package com.epam.health.tool.facade.common.service.action.yarn.searcher;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.service.action.IJarSearcher;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;
import org.slf4j.Logger;

public abstract class BaseJarSearcher implements IJarSearcher {
    protected SshAuthenticationClient sshAuthenticationClient;

    public BaseJarSearcher(SshAuthenticationClient sshAuthenticationClient) {
        this.sshAuthenticationClient = sshAuthenticationClient;
    }

    @Override
    public String searchJarPath(String jarMask, String clusterName, String possiblePath) {
        String pathToJar = findExampleJarOnPossiblePath( jarMask, clusterName, possiblePath );

        return pathToJar.isEmpty() ? searchJarPath( jarMask, clusterName ) : pathToJar;
    }

    protected abstract String searchJarPath( String jarMask, String clusterName );
    protected abstract Logger log();

    protected String findExamplesPath( String jarMask, String clusterName, String possiblePathToJar ) {
        try {
            String result = sshAuthenticationClient
                    .executeCommand( clusterName, "ls " + possiblePathToJar + " | grep " + jarMask ).getOutMessage();
            if ( !CheckingParamsUtil.isParamsNullOrEmpty( result ) ) {
                return result.contains( possiblePathToJar ) ? result.split( "\\s+" )[0].trim()
                        : possiblePathToJar.concat( "/" ).concat( result.split( "\\s+" )[0].trim() );
            }

            return StringUtils.EMPTY;
        }
        catch ( AuthenticationRequestException ex ) {
            log().error( ex.getMessage() );
            return StringUtils.EMPTY;
        }
    }

    private String findExampleJarOnPossiblePath( String jarMask, String clusterName, String possiblePath ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( possiblePath ) ? findExamplesPath( jarMask, clusterName, possiblePath )
                : StringUtils.EMPTY;
    }
}
