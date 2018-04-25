import {JobExample} from "../cluster/health/job-example.model";

export class ServiceStatus {
  displayName: string;
  healthSummary: string;
  logsLocation: string;
  jobResults: JobExample[];
}
