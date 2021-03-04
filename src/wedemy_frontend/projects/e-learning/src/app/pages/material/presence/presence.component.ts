import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationStart, Router } from '@angular/router';
import { APPROVEMENT } from '@bootcamp-elearning/constants/approvement';
import { PresenceService } from '@bootcamp-elearning/services/presence.service';
import { AuthService } from '@bootcamp-homepage/services/auth.service';

@Component({
  selector: 'app-presence',
  templateUrl: './presence.component.html',
  styleUrls: ['./presence.component.css']
})
export class PresenceComponent implements OnInit {
  idDetailModuleRegistration: string;
  idDetailClass: string;

  participants: any[] = [];

  selectedParticipants: any[] = [];
  loading: boolean = true;

  approvements = APPROVEMENT;

  activityValues: number[] = [0, 100];

  constructor(private route: ActivatedRoute,
    private presenceService: PresenceService,
    private authService: AuthService,
    private titleService: Title) { }

  ngOnInit(): void {
    this.titleService.setTitle('Kehadiran')
    this.route
      .queryParams
      .subscribe(params => {
        this.idDetailModuleRegistration = params['idDtlModuleRgs'];
        this.idDetailClass = params['idDtlClass'];
        this.getPresence();
      });
  }

  setStatusPresence(data: any, code: string) {
    let payload = { approvementRenewals: [] }

    if (Array.isArray(data)) {
      if (data.length > 0) {
        data.map(val => {
          payload.approvementRenewals.push(
            {
              idPresence: {
                id: val.idPresence.id
              },
              idApprovement: {
                code: code
              },
              createdBy: this.authService.getUserId()
            }
          )
        })
      }
    } else {
      payload.approvementRenewals.push(
        {
          idPresence: {
            id: data.idPresence.id
          },
          idApprovement: {
            code: code
          },
          createdBy: this.authService.getUserId()
        }
      )
    }

    this.presenceService.setStatusPresence(payload).subscribe(
      res => {
        this.getPresence();
      },
      err => {
        console.log(err);
      }
    )
  }

  getPresence(): void {
    let param = {
      idDtlClass: this.idDetailClass,
      idDtlModuleRgs: this.idDetailModuleRegistration
    }
    this.presenceService.getPresence(param).subscribe(
      res => {
        this.participants = res.data;
        this.loading = false;
      },
      err => {
        console.log(err);
      }
    )
  }

  refresh(): void {
    this.loading = true;
    this.getPresence();
  }

  selectAll(isCheck: boolean): void {
    if (isCheck) {
      this.selectedParticipants = this.participants.filter(val => {
        return val.idApprovement.code === this.approvements.PENDING
      });
    } else {
      this.selectedParticipants = [];
    }
  }
}
