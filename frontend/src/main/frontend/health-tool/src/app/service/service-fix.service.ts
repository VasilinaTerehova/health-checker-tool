import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

//Models
import { ServiceFixResult } from './service-fix-result.model';
import { StringWrapper } from '../shared/wrapper/string-wrapper.model';
import { environment } from '../../environments/environment';

@Injectable()
export class ServiceFixService {
  constructor(private _http: HttpClient) {  }

  public fixService( clusterName: string, serviceName: string, rootUsername: string, rootPassword: string ) {
    return this._http.get<ServiceFixResult>( "http://"+environment.htServerHost+":"+environment.htServerPort+"/api/cluster/" + clusterName + "/service/" + serviceName + "/fix",
      { params: { "rootPassword": rootPassword, "rootUsername": rootUsername} } );
  }

  public getFixstepList( clusterName: string, serviceName: string ) {
    return this._http.get<String[]>( "http://"+environment.htServerHost+":"+environment.htServerPort+"/api/cluster/" + clusterName + "/service/" + serviceName + "/manual/fix");
  }

  public generateFixBashScript( clusterName: string, serviceName: string ) {
    return this._http.get<StringWrapper>( "http://"+environment.htServerHost+":"+environment.htServerPort+"/api/cluster/" + clusterName + "/service/" + serviceName + "/generate/fix");
  }
}
