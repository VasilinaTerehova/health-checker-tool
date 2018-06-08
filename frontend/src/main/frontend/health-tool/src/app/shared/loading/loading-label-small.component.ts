import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'loading-label-small',
  templateUrl: 'loading-label-small.component.html',
})
export class LoadingLabelSmallComponent implements OnInit {
  isLoading: Boolean;
  constructor(  ) {  }

  @Input( "show" )
  set changeLoading( show: Boolean ) {
    this.isLoading = show;
  }

  ngOnInit() {}
}
