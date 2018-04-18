export class CheckHealthToken {
  constructor( public clusterName: string, public token: string = "empty", public useSave: boolean = false ) {}
}
