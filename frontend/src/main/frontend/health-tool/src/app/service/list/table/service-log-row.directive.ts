import { Directive, Input, ElementRef, OnInit, HostBinding, Renderer2 } from '@angular/core';

@Directive({ selector: '[service-log-row]' })
export class ServiceLogRowDirective {
  @Input("log-dir") logDir: string;
  @Input("log-host") logHost: string;

  constructor( private _el: ElementRef, private _renderer: Renderer2 ) {}

  ngOnInit() {
    if ( this.logDir ) {
      if ( !this.logHost || !this.logHost.search( "not found" ) ) {
        this.elementValue = "Default location: ".concat( this.logDir );
      } else {
        this.elementValue = this.logDir.concat( " on ", this.logHost );
      }
    }
  }

  set elementValue( value: string ) {
    this._el.nativeElement.innerHTML = value;
  }
}
