import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {MyAuthService} from "../auth/my-auth-service";
import {SharedService} from "../shared/services/shared.service";
import {isNull} from "util";
import {AsyncPipe} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  isLoggedIn: boolean;
  isPremium: boolean;

  //Both SharedService and ChangeDetectorRef are necessary to listen to changes on logged in variable to show different nav.
  constructor(private auth: MyAuthService, private sharedService: SharedService, private cdRef: ChangeDetectorRef) {
    this.isLoggedIn = !isNull(localStorage.getItem("X-TOKEN"));
    this.sharedService.notifyObservable$.subscribe((res) => {
      if (res.hasOwnProperty('loggedIn')) {
        this.isLoggedIn = res.loggedIn;
        this.cdRef.detectChanges();
      }
    });
  }

  public navDropdownLogoout() {
    this.auth.logout();
  }

  ngOnInit() {
    // this.isPremium = this.auth.isPremium():
  }

}
