import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Routes are added with the format { path: '',  component: DashboardComponent }
const routes: Routes = [
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRouterModule {}
