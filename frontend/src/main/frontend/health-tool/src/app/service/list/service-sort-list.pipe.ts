import { Pipe, PipeTransform } from '@angular/core';

import { ServiceStatus } from '../service-status.model';

@Pipe({
  name: 'serviceSortList'
})
export class ServiceListSortPipe implements PipeTransform {
  transform(value: ServiceStatus[], ascSort: boolean): ServiceStatus[] {
    return value ? value.sort( (one, two) => {
      if ( one.displayName > two.displayName ) {
        return ascSort ? 1 : -1;
      }
      else {
        return ascSort ? -1 : 1;
      }
    }) : value;
  }
}
