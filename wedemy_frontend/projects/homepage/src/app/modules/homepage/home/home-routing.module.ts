import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from '@bootcamp-homepage/pages/home/home.component';
import { CanActivateTeam } from '@bootcamp-homepage/shared/guards/classes/can-activate-team';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [CanActivateTeam]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
