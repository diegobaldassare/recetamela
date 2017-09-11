import { Component, OnInit } from '@angular/core';
import {MyAuthService} from "../auth/my-auth-service";

@Component({
  selector: 'app-log-out',
  templateUrl: './log-out.component.html',
  styleUrls: ['./log-out.component.css']
})
export class LogOutComponent implements OnInit {

  constructor(private auth: MyAuthService) { }

  logout() {
    console.log("now logging out");
    this.auth.logout();
  }

  ngOnInit() {
  }

}
