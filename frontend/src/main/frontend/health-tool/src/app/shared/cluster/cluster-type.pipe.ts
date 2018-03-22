import { Pipe, PipeTransform } from '@angular/core';
import { ClusterType } from './cluster-type.model';

@Pipe({
  name: 'cluster-type-select'
})
export class ClusterTypePipe implements PipeTransform {
  transform(value: ClusterType, args: any[]): any {
    var names = Object.keys( value ).filter(k => typeof ClusterType[k] === "number") as string[];
    return names;
  }
}
