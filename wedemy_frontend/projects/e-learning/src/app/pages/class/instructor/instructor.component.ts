import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { ClassService } from '@bootcamp-elearning/services/class.service';

@Component({
  selector: 'app-instructor',
  templateUrl: './instructor.component.html',
  styleUrls: ['./instructor.component.css']
})
export class InstructorComponent implements OnInit {

  idDetailClass: string;
  tutor: any;
  isLoading: boolean = true;

  constructor(private route: ActivatedRoute,
    private classService: ClassService,
    private titleService: Title) { }

  ngOnInit(): void {
    this.route.params.subscribe(param => {
      this.idDetailClass = param['idDetailClass'];
      this.getDetail();
    })
  }

  getDetail(): void {
    this.classService.getDetail(this.idDetailClass).subscribe(
      res => {
        this.tutor = res.data.idClass.idTutor;
        this.isLoading = false;
        this.titleService.setTitle(`Tutor - ${this.tutor.idProfile.fullName}`);
      },
      err => {
        console.log(err);

      }
    )
  }

}
