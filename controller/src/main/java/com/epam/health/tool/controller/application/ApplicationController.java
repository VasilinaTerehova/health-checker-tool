package com.epam.health.tool.controller.application;

import com.epam.facade.model.ApplicationInfo;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApplicationController {
    @Autowired
    private IFacadeImplResolver<IApplicationFacade> applicationFacadeIFacadeImplResolver;
    @Autowired
    private IClusterFacade clusterFacade;

    @CrossOrigin( origins = "http://localhost:4200" )
    @RequestMapping("/getApplicationList")
    public List<ApplicationInfo> getYarnAppList(@RequestParam( "clusterName") String clusterName ) {
        try {
            return applicationFacadeIFacadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterType().name() ) //Should be changed
                    .getApplicationList( clusterName );
        }
        catch ( ImplementationNotResolvedException | InvalidResponseException ex ) {
            throw new RetrievingObjectException( ex );
        }
    }

}
