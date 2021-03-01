import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DetailClasses } from '@bootcamp-homepage/models/detail-classes';
import { DetailClassService } from '@bootcamp-homepage/services/detail-class.service';


@Component({
  selector: 'app-class-read',
  templateUrl: './class-read.component.html',
  styleUrls: ['./class-read.component.css']
})
export class ClassReadComponent implements OnInit {

  searchText = '';
  listClasses: DetailClasses[] = [];
  countModule: number = 0;
  totalHours: number = 0;
  countMat: number = 0;
  loading: boolean = true;

  constructor(private router: Router,
    private dtlClassService: DetailClassService,
    public datepipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.dtlClassService.getAll()
      .subscribe(res => {
        this.listClasses = res.data;
        this.listClasses.forEach(c => {
          this.dtlClassService.getInformation(c.id).subscribe(info => {
            c.totalHours = info.data.totalHours;
            c.totalModules = info.data.modules.length;
            this.showStatus(c);
            this.loading = false;
          })
        })
      });
  }

  showDetail(id: string): void {
    this.router.navigateByUrl(`/class/${id}`);
  }


  showStatus(c: DetailClasses): void{
    let today: Date = new Date();
    today.setHours(0, 0, 0, 0);
    let todayFormatted = new Date(this.datepipe.transform(today, 'yyyy-MM-dd'))

    let end = new Date(c.endDate);
    let start = new Date(c.startDate);
    if(todayFormatted < start) {
      c.status = 1;
    } else if (todayFormatted >= start) {
      c.status = 2;
    }
  }
}
