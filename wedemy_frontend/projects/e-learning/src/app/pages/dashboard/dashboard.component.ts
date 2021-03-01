import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ROLE } from '@bootcamp-core/constants/role';
import { SELECTED_TYPE } from '@bootcamp-elearning/constants/selected-type';
import { DashboardService } from '@bootcamp-elearning/services/dashboard.service';
import { AuthService } from '@bootcamp-homepage/services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  query: string = '';
  classes: any[] = [];
  instructors: any[] = [];
  isLoading: boolean = true;

  results: any[] = [];
  selectedInstructor = SELECTED_TYPE.ALL;

  ROLES = ROLE;
  myRole: string;

  defaultImg: string = "/assets/img/profile-default.jpeg";

  constructor(private dashboardService: DashboardService,
    private authService: AuthService,
    private titleService: Title) {
    this.titleService.setTitle('Dashboard');
  }

  ngOnInit(): void {
    this.getMyClass();
  }

  getMyClass(): void {
    let userId: string = this.authService.getUserId();
    let rolecode: string = this.authService.getRole();
    this.myRole = rolecode;
    this.dashboardService.getMyClass(userId, rolecode).subscribe(
      res => {
        this.classes = res.data;
        this.results = res.data;
        this.isLoading = false;
        this.getUniqueInstructor();
      },
      err => { console.log(err) }
    )
  }

  getUniqueInstructor(): void {
    const uniqueInstructors = [...new Set(this.classes.map(item => item.idClass
      .idTutor
      .idProfile.fullName))];
    this.instructors = uniqueInstructors;
  }

  search(): void {
    this.results = this.classes.filter((item) => {
      if (this.selectedInstructor == SELECTED_TYPE.ALL) {
        return item
          .idClass
          .className
          .toLowerCase().indexOf(this.query.toLowerCase()) !== -1;
      } else {
        return item
          .idClass
          .className
          .toLowerCase().indexOf(this.query.toLowerCase()) !== -1
          && item.idClass.idTutor
            .idProfile
            .fullName.toLowerCase().indexOf(this.selectedInstructor.toLowerCase()) !== -1;
      }
    })
  }

  counter(n: number): any[] {
    return new Array(n);
  }
}
