import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClassService } from '@bootcamp-elearning/services/class.service';
import { ROLE } from '@bootcamp-homepage/constants/roles';
import { AuthService } from '@bootcamp-homepage/services/auth.service';
import { VIEW_TYPE } from '../../../constants/view-type';
import * as moment from 'moment';
import { PresenceService } from '@bootcamp-elearning/services/presence.service';
import { ConfirmationService } from 'primeng/api';
import { MaterialService } from '@bootcamp-elearning/services/material.service';
import { Title } from '@angular/platform-browser';
import { MATERIAL_TYPES } from '@bootcamp-elearning/constants/meterial-type';

@Component({
  selector: 'app-module',
  templateUrl: './module.component.html',
  styleUrls: ['./module.component.css'],
})
export class ModuleComponent implements OnInit {
  idDetailClass: string;
  roleCode: string;

  roles = ROLE;
  materialTypes = MATERIAL_TYPES;
  viewType = VIEW_TYPE;
  selectedView = VIEW_TYPE.VIEW_ONLY;

  modules: any;
  isLoading: boolean = true;

  constructor(private route: ActivatedRoute,
    private authService: AuthService,
    private confirmationService: ConfirmationService,
    private classService: ClassService,
    private materialService: MaterialService,
    private presenceService: PresenceService,
    private titleService: Title) { }

  ngOnInit(): void {
    this.roleCode = this.authService.getRole();

    this.route.params.subscribe(params => {
      this.idDetailClass = params['idDetailClass'];
      this.getDetail(this.idDetailClass);
    })
  }

  getDetail(idDtlClass: string): void {
    let idUser = this.authService.getUserId();
    let params = { idUser, idDtlClass }

    this.classService.getDetailModule(params).subscribe(
      res => {
        this.modules = res.data;
        this.checkPresent();
        this.titleService.setTitle(`Kurikulum Kelas - ${this.modules.detailClass.idClass.className}`);
      },
      err => { console.log(err) }
    )
  }

  checkPresent(): void {
    let startTime = this.modules.detailClass.startTime;
    let endTime = this.modules.detailClass.endTime;
    this.modules.modulesAndMaterials.forEach(val => {
      val.learningMaterials.forEach(learningMaterial => {
        if (this.roleCode === ROLE.PARTICIPANT) {
          if (!learningMaterial.doesParticipantPresent) {
            if (learningMaterial.isUserOnTime) {
              if (learningMaterial.doesTutorPresent) {
                learningMaterial.statusPresent = {
                  type: 'btn',
                  style: 'success',
                  msg: 'Hadir',
                  status: 400
                }
              } else {
                learningMaterial.statusPresent = {
                  type: 'badge',
                  style: 'info',
                  msg: 'Tutor Belum Menghadiri Kelas',
                  status: 400
                }
              }
            } else {
              let scheduleDate = learningMaterial.learningMaterial.scheduleDate;
              let scheduleStarDateTime = moment(`${scheduleDate} ${startTime}`, "YYYY-MM-DD HH:mm");
              let scheduleEndDateTime = moment(`${scheduleDate} ${endTime}`, "YYYY-MM-DD HH:mm");
              let dateTimeNow = moment(new Date());

              if (dateTimeNow.diff(scheduleStarDateTime, 'seconds') >= 0) {
                if (dateTimeNow.diff(scheduleEndDateTime, 'seconds') > 0) {
                  learningMaterial.statusPresent = {
                    type: 'badge',
                    style: 'danger',
                    msg: 'Anda Terlambat Menghadiri Kelas',
                    status: 400
                  }
                }
              } else {
                learningMaterial.statusPresent = {
                  type: 'badge',
                  style: 'warning',
                  msg: 'Kelas Belum Dimulai',
                  status: 400
                }
              }
            }
          } else {
            if (learningMaterial.isParticipantConfirmed) {
              if (learningMaterial.isParticipantAccepted) {
                learningMaterial.statusPresent = {
                  type: 'badge',
                  style: 'success',
                  msg: 'Telah Menghadiri Kelas',
                  status: 200
                }
              } else {
                learningMaterial.statusPresent = {
                  type: 'badge',
                  style: 'danger',
                  msg: 'Tutor menolak kehadiran anda',
                  status: 400
                }
              }
            } else {
              learningMaterial.statusPresent = {
                type: 'badge',
                style: 'info',
                msg: 'Menunggu Konfirmasi Tutor',
                status: 400
              }
            }
          }
        } else {
          if (!learningMaterial.doesTutorPresent) {
            if (learningMaterial.isUserOnTime) {
              learningMaterial.statusPresent = {
                type: 'btn',
                style: 'success',
                msg: 'Hadir',
                status: 400
              }
            } else {
              let scheduleDate = learningMaterial.learningMaterial.scheduleDate;
              let scheduleStarDateTime = moment(`${scheduleDate} ${startTime}`, "YYYY-MM-DD HH:mm");
              let scheduleEndDateTime = moment(`${scheduleDate} ${endTime}`, "YYYY-MM-DD HH:mm");
              let dateTimeNow = moment(new Date());
              if (dateTimeNow.diff(scheduleStarDateTime, 'seconds') >= 0) {
                if (dateTimeNow.diff(scheduleEndDateTime, 'seconds') > 0) {
                  learningMaterial.statusPresent = {
                    type: 'badge',
                    style: 'danger',
                    msg: 'Anda Terlambat',
                    status: 400
                  }
                }
              } else {
                learningMaterial.statusPresent = {
                  type: 'badge',
                  style: 'warning',
                  msg: 'Kelas Belum Dimulai',
                  status: 400
                }
              }
            }
          } else {
            learningMaterial.statusPresent = {
              type: 'badge',
              style: 'success',
              msg: 'Telah Menghadiri Kelas',
              status: 200
            }
          }
        }
      });
    });
    this.isLoading = false;
  }

  present(idDetailModuleRegistration: string): void {
    let data = {
      idDetailModuleRegistration: {
        id: idDetailModuleRegistration
      },
      idUser: {
        id: this.authService.getUserId()
      }
    }

    this.presenceService.presence(data).subscribe(
      res => {
        this.refresh();
      },
      err => {
        console.log(err);
      }
    )
  }

  onChangeOperation(e): void {
    this.selectedView = e;
  }

  deleteMaterial(idLearningMaterial: string) {
    this.confirmationService.confirm({
      message: 'Apakah anda yakin menghapus materi ini?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.materialService.removeMaterial(idLearningMaterial, this.authService.getUserId()).subscribe(
          res => {
            this.refresh();
          },
          err => {
            console.log(err);
          }
        )
      }
    });
  }

  refresh(): void {
    this.isLoading = true;
    this.getDetail(this.idDetailClass);
  }
}
