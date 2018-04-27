import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'cluster-fix-issues',
  templateUrl: './cluster-fix-issues.component.html',
})
export class ClusterFixIssuesComponent implements OnInit {
  @Input() clusterName: String;

  constructor() {  }

  ngOnInit() {}

  get yarnFixOperationName(): String {
    return "clean YARN cache dirs";
  }
}
