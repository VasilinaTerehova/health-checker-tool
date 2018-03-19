import { RouterModule, Routes } from '@angular/router';

import {HomeComponent} from './home/home.component';
import { ClusterComponent } from './cluster/cluster.component';

const appRoutes: Routes = [
   { path: '', component: HomeComponent },
   { path: 'cluster/:id', component: ClusterComponent },

   // otherwise redirect to home
   { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes, { useHash: true });
