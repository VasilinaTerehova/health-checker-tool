import {JobExample} from '../cluster/health/job-example.model';

export class ServiceStatus {
  constructor( public displayName: string = "", public healthSummary: string = "",
    public logDirectory: string = "", public clusterNode: string = "", public jobResults: JobExample[] = new Array<JobExample>() ) {}
}
