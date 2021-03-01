import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AboutComponent } from '@bootcamp-homepage/pages/about/about.component';
import { CanActivateTeam } from '@bootcamp-homepage/shared/guards/classes/can-activate-team';

const routes: Routes = [
  {
    path: 'about',
    component: AboutComponent,
    canActivate: [CanActivateTeam]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AboutRoutingModule { }
