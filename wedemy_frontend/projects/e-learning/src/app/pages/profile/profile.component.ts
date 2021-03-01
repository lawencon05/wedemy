import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { User } from '@bootcamp-elearning/models/user';
import { ProfileService } from '@bootcamp-elearning/services/profile.service';
import { UserService } from '@bootcamp-elearning/services/user.service';
import { AuthService } from '@bootcamp-homepage/services/auth.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MessageService } from 'primeng/api';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  myAccount: User = new User();
  displayMaximizable: boolean;
  defaultImg: string = "/assets/img/profile-default.jpeg";
  url: any = "/assets/img/profile-default.jpeg";
  uploadForm: FormGroup;
  formData = new FormData();
  file: String;

  phoneIsValid: boolean;
  phoneErrMsg: string;

  ktpIsValid: boolean;
  ktpErrMsg: string;

  isLoading: boolean = true;
  yearRange: number;

  birthDateIsValid: boolean;
  birthDateErrMsg: string;

  disabledButton: boolean = false;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private profileService: ProfileService,
    private datepipe: DatePipe,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.userService.getUserById(this.authService.getUserId()).subscribe(res => {
      this.myAccount = res.data;
      this.isLoading = false;
      this.getYearRange();
    })
  }

  getYearRange(): void {
    let now = new Date().getFullYear();
    this.yearRange = now-1;
  }

  showMaximizableDialog() {
    if (this.myAccount.idProfile.idFile.file) {
      this.url = 'data:image/png;base64,' + this.myAccount.idProfile.idFile.file
    }
    this.displayMaximizable = true;
  }

  onSelectFile(event) {

    let fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      let file: File = fileList[0];
      let data: FormData = new FormData();
      data.append('file', file);
      this.formData = data;
      this.file = file.name;
    }

    if (event.target.files.length > 0) {
      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0])
      reader.onload = (event) => {
        this.url = event.target.result;
      }
    }
  }

  onSubmit() {
    if (this.myAccount.idProfile.birthDate) {
      let birthDateFormatted = this.datepipe.transform(this.myAccount.idProfile.birthDate, 'yyyy-MM-dd')
      this.myAccount.idProfile.birthDate = birthDateFormatted.toString();
    }
    this.myAccount.idProfile.updatedBy = this.authService.getUserId();

    this.formData.append('body', JSON.stringify(this.myAccount.idProfile));
    this.profileService.updateProfile(this.formData).subscribe(res => {
      this.myAccount.idProfile = res.data;
      this.displayMaximizable = false;
      this.formData.delete('body');
      this.ngOnInit();
    })
  }


  phoneValidation(event: string): void {
    if (/^[0-9]*$/.test(event) && (event.length > 10 && event.length < 13)) {
      this.phoneIsValid = true;
    } else {
      this.phoneIsValid = false;
      if (!/^[0-9]*$/.test(event)) {
        this.phoneErrMsg = "Masukkan angka saja"
      }
      else if (event.length < 11 || event.length > 12) {
        this.phoneErrMsg = "Minimal 11 digit, maksimal 12 digit"
      }
    }
  }

  numIdentity(event: string): void {
    if (/^[0-9]*$/.test(event) && event.length == 16) {
      this.ktpIsValid = true;
      this.disabledButton = false;
    } else {
      this.ktpIsValid = false;
      this.disabledButton = true;
      if (!/^[0-9]*$/.test(event)) {
        this.ktpErrMsg = "Masukkan angka saja"
      }
      else if (event.length < 16 || event.length > 16) {
        this.ktpErrMsg = "Nomor identitas harus 16 angka"
      }
    }
  }

  birthDateValidation(event: string): void {
    let now = new Date();
    now.setHours(0);
    now.setMinutes(0);
    now.setSeconds(0);
    let birthDate = new Date(event);
    if (birthDate >= now || birthDate.toString() == now.toString()) {
      this.birthDateIsValid = false;
      this.disabledButton = true;
      this.birthDateErrMsg = "Tanggal lahir tidak boleh lebih dari sama dengan hari ini "
    } else {
      this.birthDateIsValid = true;
      this.disabledButton = false;
    }
  }

  refresh(): void {
    this.userService.getUserById(this.authService.getUserId()).subscribe(res => {
      this.myAccount = res.data;
    })
  }
}
