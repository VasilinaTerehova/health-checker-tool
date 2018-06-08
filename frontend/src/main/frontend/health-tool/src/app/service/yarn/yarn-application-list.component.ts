import { Component, Input, Output, EventEmitter } from '@angular/core';

//Models
import { YarnApplication } from './application.model';
import { CheckHealthToken } from '../../cluster/health/check-health-token.model';
import { ErrorAlert } from '../../shared/error/error-alert.model';
import { AlertType } from '../../shared/error/alert-type.model';
//Services
import { YarnApplicationService } from './yarn-application.service';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';

@Component({
  selector: 'yarn-application-list',
  templateUrl: 'yarn-application-list.component.html',
})
export class YarnApplicationListComponent {
  @Output() onYarnAppsChange = new EventEmitter<number>();
  yarnApps: YarnApplication[];
  _checkHealthToken: CheckHealthToken
  _stateArr: boolean[] = Array();
  _selectedAppsArr: number[] = Array();
  _checkedEventArr: any[] = Array();
  showBtn = -1;
  isLoading: Boolean;
  killJob: Boolean;
  hideCancel: Boolean;

  constructor(private yarnApplicationService: YarnApplicationService, private errorReportingService: ErrorReportingService) { }

  @Input()
  set checkHealthToken(checkHealthToken: CheckHealthToken) {
    if (checkHealthToken) {
      this.isLoading = true;
      this._checkHealthToken = checkHealthToken;
      this.askForYarnAppsList(checkHealthToken);
    }
  }

  private askForYarnAppsList(checkHealthToken: CheckHealthToken) {
    this.yarnApplicationService.getYarnApps(checkHealthToken.clusterName, checkHealthToken.token).subscribe(
      data => {
        this.yarnApps = data;
        this.isLoading = false;
        this.onYarnAppsChange.emit(this.yarnApps.length);
      },
      error => this.errorReportingService.reportHttpError(error)
    )
  }

  public getYarnAppList() {
    this.isLoading = true;
    this.askForYarnAppsList(this._checkHealthToken);
  }

  private updateKilledYarnAppState(index: number) {
    this.yarnApps[index].state = "KILLED";
  }

  public killApp(appId: string, index: number) {
    this.showBtn = index;
    this.yarnApplicationService.kilApp(this._checkHealthToken.clusterName, appId).subscribe(
      result => {
        this._stateArr = result;
        this.showBtn = -1;

        if (this._stateArr[0]) {
          this.formResultSuccessKilledAppsMessage(appId);
          this.updateKilledYarnAppState(index);
        } else {
          this.formResultErrorKilledAppsMessage(appId);
        }
      },
      error => this.errorReportingService.reportHttpError(error)
    )
  }

  public killSelectedApps() {
    this.hideCancelButton();
    var appIds = this.selectAppsFromYarnAppsListByIndex();
    if (appIds == "") {
      this.errorReportingService.reportError(new ErrorAlert("Applications are not selected. Select Applications to kill.", AlertType.DANGER));
      this.showCancelButton();
      return;
    }
    this.yarnApplicationService.kilApp(this._checkHealthToken.clusterName, appIds).subscribe(
      result => {
        this.processGroupKillResult(result);
      },
      error => this.errorReportingService.reportHttpError(error)
    )
  }

  private processGroupKillResult(killResultArr: boolean[]) {
    var notKilledApps: string = "";
    var killedApps: string = "";

    for (var i = 0; i < killResultArr.length; i++) {
      if (killResultArr[i]) {
        killedApps = killedApps.concat(this.yarnApps[this._selectedAppsArr[i]].applicationId).concat(" ");
        this.updateKilledYarnAppState(this._selectedAppsArr[i]);
      } else {
        notKilledApps = notKilledApps.concat(this.yarnApps[this._selectedAppsArr[i]].applicationId).concat(" ");
      }
    }
    this.formResultKillAppsMessage(killedApps, notKilledApps);
    this.uncheckSelectedApps();
    this.resetSelectedAppsArr();
    this.showCancelButton();
  }

  public cancelKillApps() {
    this.killJob = false;
    this.resetSelectedAppsArr();
    if (this._checkedEventArr.length > 0) {
      this.uncheckSelectedApps();
    }
  }

  private showCancelButton() {
    this.hideCancel = false;
  }

  private hideCancelButton() {
    this.hideCancel = true;
  }

  private formResultErrorKilledAppsMessage(notKilledApps: string) {
    this.errorReportingService.reportError(new ErrorAlert("Application(s) " + notKilledApps + " already finished!", AlertType.WARNING));
  }

  private formResultSuccessKilledAppsMessage(killedApps: string) {
    this.errorReportingService.reportError(new ErrorAlert("Application(s) " + killedApps + " successfully killed!", AlertType.SUCCESS));
  }

  private formResultKillAppsMessage(killedApps: string, notKilledApps: string) {
    if (killedApps != "") {
      this.formResultSuccessKilledAppsMessage(killedApps);
    }
    if (notKilledApps != "") {
      this.formResultErrorKilledAppsMessage(notKilledApps);
    }
  }

  public fillArrWithCheckedAppsByIndex(appIndex: number, event: any) {
    if (event.target.checked) {
      this._checkedEventArr[this._checkedEventArr.length] = event;
      this._selectedAppsArr[this._selectedAppsArr.length] = appIndex;
    } else {
      var index = this._selectedAppsArr.indexOf(appIndex, 0);
      if (index > -1) {
        this._selectedAppsArr.splice(index, 1);
      }
    }
  }

  public checkAppAvailableToKill(state: string): Boolean{
    return (state=='NEW' || state=='ACCEPTED' || state=='RUNNING');
  }

  private uncheckSelectedApps() {
    for (var i = 0; i < this._checkedEventArr.length; i++) {
      this._checkedEventArr[i].target.checked = false;
    }
    this.resetCheckedEventArr();
  }

  private resetCheckedEventArr() {
    this._checkedEventArr = [];
  }

  private resetSelectedAppsArr() {
    this._selectedAppsArr = [];
  }

  private selectAppsFromYarnAppsListByIndex(): string {

    var appsIndexes: string = "";

    for (var i = 0; i < this._selectedAppsArr.length; i++) {
      appsIndexes = appsIndexes.concat(this.yarnApps[this._selectedAppsArr[i]].applicationId).concat(",");
    }
    return appsIndexes;
  }
}
