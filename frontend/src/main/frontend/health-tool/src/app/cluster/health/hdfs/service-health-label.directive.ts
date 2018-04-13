import { Directive, Input, OnInit, HostBinding } from '@angular/core';

@Directive({ selector: '[service-health-label]' })
export class ServiceHealthLabelDirective {
  private _healthSummary: string;
  private elementClasses: string[] = [];

  constructor() {}

  ngOnInit() {
    this.generateElementClasses();
  }

  @Input()
  set healthSummary( healthSummary: string ) {
    this._healthSummary = healthSummary;
    this.generateElementClasses();
  }

  get healthSummary(): string {
    return this._healthSummary;
  }

  @HostBinding('class')
  get getElementClass(): string {
    return this.elementClasses.join( ' ' );
  }

  private generateElementClasses() {
    this.elementClasses = ["label"];
    switch ( this._healthSummary )   {
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
}
