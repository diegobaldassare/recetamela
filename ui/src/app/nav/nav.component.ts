import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MyAuthService} from "../auth/my-auth-service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  isLoggedIn = false;
  isPremium: boolean;

  constructor(private auth: MyAuthService) { }

  ngOnInit() {
    this.isLoggedIn = this.auth.isLoggedIn();
    // this.isPremium = this.auth.isPremium():

  }
}
