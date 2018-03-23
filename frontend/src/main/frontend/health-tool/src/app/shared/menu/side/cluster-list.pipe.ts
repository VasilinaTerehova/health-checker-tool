import { Pipe, PipeTransform } from '@angular/core';

import { Cluster } from '../../cluster/cluster.model';

@Pipe({
  name: 'clusterListSearchByName',
  pure: false
})
export class ClusterListSearchByNamePipe implements PipeTransform {
  transform( clusters: Cluster[], filter: Object ): any {
    if ( !clusters ) {
      return clusters;
    }
    if ( !filter ) {
      return clusters.sort( (one, two) => (one.name < two.name ? -1 : 1) );
    }

    return clusters.filter( item => item.name.includes( filter.toString() ) ).sort( (one, two) => (one.name < two.name ? -1 : 1) );
  }
}
