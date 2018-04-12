import { Credentials } from './credentials.model';
import {HdfsUsage} from "./hdfs.model";
import {Memory} from "./memory.model";
import {NodeFs} from "./NodeFs";

export class Cluster {
  id: number;
  name: string = "";
  clusterType: string = "";
  host: string = "";
  dateOfSnapshot:Date = null;
  secured: boolean = false;
  http: Credentials = new Credentials();
  ssh: Credentials = new Credentials();
  kerberos: Credentials = new Credentials();
  hdfsUsage: HdfsUsage = new HdfsUsage();
  memoryUsage: Memory = new Memory();
  nodes: NodeFs[];
}
