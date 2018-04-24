import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
/*Bootstrap modules*/
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ButtonsModule } from 'ngx-bootstrap/buttons';
import { AlertModule } from 'ngx-bootstrap/alert';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { CollapseModule } from 'ngx-bootstrap/collapse';
//Components
import { AppComponent } from './app.component';
import { TopMenuComponent } from './shared/menu/top/top-menu.component';
import { SideBarComponent } from './shared/menu/side/sidebar.component';
import { HomeComponent } from './home/home.component';
import { ClusterComponent } from './cluster/cluster.component';
import { ErrorReportingComponent } from './shared/error/error-reporting.component';
import { YarnApplicationListComponent } from './service/yarn/yarn-application-list.component';
import { ServiceListComponent } from './service/list/service-list.component';
import { ClusterEditComponent } from './shared/cluster/edit/cluster-edit.component';
import { ConfirmModalComponent } from './shared/modal/confirm/confirm-modal.component';
import { CommonClusterHealthSummaryComponent } from './cluster/health/common/common-cluster-health-summary.component';
import { HdfsClusterHealthSummaryComponent } from './cluster/health/hdfs/hdfs-cluster-health-summary.component';
import { ServiceListHistoryComponent } from "./service/service-status-history/service-status-history.component";
import { ServiceListStaticComponent } from "./service/list/static/service-list-static.component";
import { CommonStaticClusterHealthSummaryComponent } from "./cluster/health/common/static/common-static-cluster-health-summary.component";
import { LoadingLabelComponent } from "./shared/loading/loading-label.component";
import { CommonClusterHealthComponent } from './cluster/health/common/common-cluster-health.component';
//Directives and pipes
import { ClusterListSearchByNamePipe } from './shared/menu/side/cluster-list.pipe';
import { ServiceTableRowDirective } from './service/list/table/service-table-row.directive';
import { ServiceListSortPipe } from './service/list/service-sort-list.pipe';
import { ServiceHealthLabelDirective } from './cluster/health/hdfs/service-health-label.directive';
//Services
import { ClusterService } from './cluster/cluster.service';
import { YarnApplicationService } from './service/yarn/yarn-application.service';
import { RouteService } from './shared/menu/side/route.service';
import { ErrorReportingService } from './shared/error/error-reporting.service';
import { ClusterTypeExService } from './shared/cluster/cluster-type-ex.service';
import { ClusterComparatorService } from './shared/cluster/cluster-comparator.service';
import { ClusterHealthCheckService } from "./cluster/health/cluster-health-check.service";
//Routing
import { routing } from './app-routing.module';


export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent, TopMenuComponent, SideBarComponent, HomeComponent, ClusterListSearchByNamePipe, ClusterComponent, ErrorReportingComponent, YarnApplicationListComponent,
    ServiceListComponent, ServiceListStaticComponent, ServiceListHistoryComponent, ClusterEditComponent, ConfirmModalComponent, ServiceTableRowDirective, ServiceListSortPipe,
    CommonClusterHealthSummaryComponent, HdfsClusterHealthSummaryComponent, ServiceHealthLabelDirective, CommonStaticClusterHealthSummaryComponent, LoadingLabelComponent, CommonClusterHealthComponent
  ],
  entryComponents: [ClusterEditComponent, ConfirmModalComponent],
  imports: [
    BrowserModule, FormsModule, HttpClientModule, routing, TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }), TabsModule.forRoot(), ButtonsModule.forRoot(), AlertModule.forRoot(), ModalModule.forRoot(), AccordionModule.forRoot(), CollapseModule.forRoot()
  ],
  providers: [ClusterService, YarnApplicationService, RouteService, ErrorReportingService, ClusterTypeExService, ClusterComparatorService, ClusterHealthCheckService],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(translate: TranslateService) {
    translate.setDefaultLang('en');
    translate.use('en');
  }
}
