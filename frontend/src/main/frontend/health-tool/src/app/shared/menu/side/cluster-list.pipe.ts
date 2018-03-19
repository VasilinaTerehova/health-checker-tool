import { Pipe, PipeTransform } from '@angular/core';

import { Cluster } from '../../cluster/cluster.model';

@Pipe({
  name: 'clusterListSearchByName',
  pure: false
})
export class ClusterListSearchByNamePipe implements PipeTransform {
  transform( clusters: Cluster[], filter: Object ): any {
    if ( !clusters || !filter ) {
      return clusters;
    }

    return clusters.filter( item => item.name.includes( filter.toString() ) );
  }
}
