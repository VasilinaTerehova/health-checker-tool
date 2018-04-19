package com.epam.health.tool.controller.application;

import com.epam.facade.model.ApplicationInfo;
import com.epam.health.tool.controller.BaseFacadeResolvingController;
import com.epam.health.tool.exception.RetrievingObjectException;
import com.epam.health.tool.facade.application.IApplicationFacade;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController extends BaseFacadeResolvingController {
    @Autowired
    private IFacadeImplResolver<IApplicationFacade> applicationFacadeIFacadeImplResolver;

    @CrossOrigin( origins = "http://localhost:4200" )
    @GetMapping("/api/cluster/{name}/applications")
    public List<ApplicationInfo> getYarnAppList( @PathVariable( "name") String clusterName ) {
        try {
            return resolveClusterSnapshotFacade( clusterName, applicationFacadeIFacadeImplResolver ).getApplicationList( clusterName );
        }
        catch ( ImplementationNotResolvedException | InvalidResponseException ex ) {
            throw new RetrievingObjectException( ex );
        }
    }

}
