import { Directive, Input, ElementRef, OnInit, HostBinding, Renderer2 } from '@angular/core';

@Directive({
  selector: '[service-table-row]'
})
export class ServiceTableRowDirective implements OnInit {
  @Input() serviceState: string;
  private elementClasses: string[] = [];

  constructor( private el: ElementRef, private renderer: Renderer2 ) {}

  ngOnInit() {
    switch ( this.serviceState )   {
      case "GOOD" : {
        this.elementClasses = ["bg-success"];
        break;
      }
      case "BAD" : {
        this.elementClasses = ["bg-danger"];
        break;
      }
      case "CONCERNING" : {
        this.elementClasses = ["bg-primary"];
        break;
      }
      default : {
        this.elementClasses = ["bg-warning"];
        break;
      }
    }
  }

  @HostBinding('class')
  get getElementClass(): string {
    return this.elementClasses.join( ' ' );
  }
}
