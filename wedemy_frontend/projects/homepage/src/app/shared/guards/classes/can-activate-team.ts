import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { Permissions } from './permissions';
import { AuthService } from "@bootcamp-homepage/services/auth.service";
import { ROLE } from "@bootcamp-homepage/constants/roles";

@Injectable()
export class CanActivateTeam implements CanActivate {
  constructor(private permissions: Permissions,
    private authService: AuthService,
    public router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let permitted: boolean = this.permissions.canActivate(this.authService.getToken(), this.authService.getRole(), route.url.toString());

    if (!permitted) {
      if (this.authService.getRole() == ROLE.ADMIN || this.authService.getRole() == ROLE.SPRADMIN) {
          this.router.navigate(['admin/dashboard']);
      } else {
        this.router.navigate(['home']);
      }
    } 
    return permitted;
  }
}