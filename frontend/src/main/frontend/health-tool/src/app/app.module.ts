import { BrowserModule } from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import { NgModule } from '@angular/core';
import {HttpClientModule, HttpClient} from '@angular/common/http';
import {TranslateModule, TranslateLoader, TranslateService} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
/*Bootstrap modules*/
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ButtonsModule } from 'ngx-bootstrap/buttons';
import { AlertModule } from 'ngx-bootstrap/alert';
//Components
import { AppComponent } from './app.component';
import { TopMenuComponent } from './shared/menu/top/top-menu.component';
import { SideBarComponent } from './shared/menu/side/sidebar.component';
import { HomeComponent} from './home/home.component';
import { ClusterComponent } from './cluster/cluster.component';
import { ErrorReportingComponent } from './shared/error/error-reporting.component';
import { YarnApplicationListComponent } from './service/yarn/yarn-application-list.component';
import { ServiceListComponent } from './service/list/service-list.component';
//Directives and pipes
import { ClusterListSearchByNamePipe } from './shared/menu/side/cluster-list.pipe';
//Services
import { ClusterService } from './cluster/cluster.service';
import { YarnApplicationService } from './service/yarn/yarn-application.service';
import { RouteService } from './shared/menu/side/route.service';
import { ErrorReportingService } from './shared/error/error-reporting.service';
//Routing
import { routing } from './app-routing.module';

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent, TopMenuComponent, SideBarComponent, HomeComponent, ClusterListSearchByNamePipe, ClusterComponent, ErrorReportingComponent, YarnApplicationListComponent,
    ServiceListComponent
  ],
  imports: [
    BrowserModule, FormsModule, HttpClientModule, routing, TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [HttpClient]
        }
    }), TabsModule.forRoot(), ButtonsModule.forRoot(), AlertModule.forRoot()
  ],
  providers: [ClusterService, YarnApplicationService, RouteService, ErrorReportingService],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor( translate: TranslateService ) {
    translate.setDefaultLang('en');
    translate.use('en');
  }
}
