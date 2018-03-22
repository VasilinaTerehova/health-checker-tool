import { Injectable } from '@angular/core';

import { ClusterType } from './cluster-type.model';

@Injectable()
export class ClusterTypeExService {
  constructor() {  }

  public etClusterTypes(): string[] {
    return Object.keys( ClusterType ).filter(k => typeof ClusterType[k] === "number") as string[];
  }
}
