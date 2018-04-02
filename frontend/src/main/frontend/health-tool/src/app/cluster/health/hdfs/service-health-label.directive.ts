import { Directive, Input, OnInit, HostBinding } from '@angular/core';

@Directive({ selector: '[service-health-label]' })
export class ServiceHealthLabelDirective {
  @Input() healthSummary: string;
  private elementClasses: string[] = [];

  constructor() {}

  ngOnInit() {
    this.elementClasses = ["label"];
    switch ( this.healthSummary )   {
      case "GOOD" : {
        this.elementClasses.push("label-success");
        break;
      }
      case "BAD" : {
        this.elementClasses.push("label-danger");
        break;
      }
      case "CONCERNING" : {
        this.elementClasses.push("label-primary");
        break;
      }
      default : {
        this.elementClasses.push("label-warning");
        break;
      }
    }
  }

  @HostBinding('class')
  get getElementClass(): string {
    return this.elementClasses.join( ' ' );
  }
}
