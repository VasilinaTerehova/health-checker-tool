import { JobExample } from '../job-example.model';

export class HdfsHealthReport {
  constructor( public healthSummary: string, public logsLocation: string, public jobResults: JobExample[] ) {}
}
