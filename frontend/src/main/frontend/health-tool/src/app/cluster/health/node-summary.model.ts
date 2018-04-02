import { NodeMemory } from './node-memory-summary.model';

export class NodeSummary {
  constructor( public host: string, public memory: NodeMemory ){}
}
