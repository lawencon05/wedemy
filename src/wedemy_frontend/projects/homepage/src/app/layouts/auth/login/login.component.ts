import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ROLE } from '@bootcamp-homepage/constants/roles';
import { Users } from '@bootcamp-homepage/models/users';
import { AuthService } from '@bootcamp-homepage/services/auth.service';
import { UserService } from '@bootcamp-homepage/services/user.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: Users = new Users();

  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
  }

  login(): void {
    this.userService.login(this.user).subscribe(val => {
      this.authService.saveToken(val.token);
      this.authService.saveProfile(val.profile, this.user);
      
      if (this.authService.getRole() == ROLE.PARTICIPANT) {
        this.router.navigateByUrl('/')
      } else if (this.authService.getRole() == ROLE.ADMIN) {
        this.router.navigateByUrl('/admin/dashboard');
      } else if (this.authService.getRole() == ROLE.TUTOR) {
        this.router.navigateByUrl('/');
      } else if (this.authService.getRole() == ROLE.SPRADMIN) {
        this.router.navigateByUrl('/admin/dashboard');
      }
    })
  }

}
