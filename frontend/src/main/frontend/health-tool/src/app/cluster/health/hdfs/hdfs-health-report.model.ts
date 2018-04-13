import { JobExample } from '../job-example.model';

export class HdfsHealthReport {
  constructor( public status: string, public logsLocation: string, public jobResults: JobExample[] ) {}
}
