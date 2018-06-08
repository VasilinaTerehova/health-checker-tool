import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { YarnApplication } from './application.model';
import { environment } from '../../../environments/environment';

@Injectable()
export class YarnApplicationService {
  constructor(private http: HttpClient) { }

  getYarnApps(clusterName: string, token: string = "empty", useSave: boolean = false) {
    return this.http.get<Array<YarnApplication>>("http://" + environment.htServerHost + ":" + environment.htServerPort + "/api/cluster/" + clusterName + "/applications", { params: { "token": token, "useSave": useSave.toString() } })
  }

  kilApp(clusterName: string, id: string) {
    return this.http.get<Array<boolean>>("http://" + environment.htServerHost + ":" + environment.htServerPort + "/api/cluster/" + clusterName + "/applications/kill", { params: { "id": id } })
  }

  kilAppList(clusterName: string, id: string) {
    return this.http.get<Array<Boolean>>("http://" + environment.htServerHost + ":" + environment.htServerPort + "/api/cluster/" + clusterName + "/applications/killselected", { params: { "id": id } })
  }
}
