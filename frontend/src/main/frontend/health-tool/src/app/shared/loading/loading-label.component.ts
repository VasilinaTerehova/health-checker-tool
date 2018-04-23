import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'loading-label',
  templateUrl: 'loading-label.component.html',
})
export class LoadingLabelComponent implements OnInit {
  isLoading: Boolean;
  constructor(  ) {  }

  @Input( "show" )
  set changeLoading( show: Boolean ) {
    this.isLoading = show;
  }

  ngOnInit() {}
}
