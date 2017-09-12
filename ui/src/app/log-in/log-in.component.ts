import { Component, OnInit } from '@angular/core';
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {LoginData} from "../shared/models/login-data";
declare const FB: any;

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit {

  constructor(private userService: UserService) { }

  me() {
    FB.api('/me?fields=id,name,email,first_name,last_name,birthday,gender,picture.width(150).height(150),age_range,friends',
      (result) => {
        if (result && !result.error) {
          //Send backend the Login Data we got from the Facebook Response
          var loginData = new LoginData(FB.getAuthResponse()['accessToken'],
            result.email,
            result.name,
            result.gender,
            result.id);
          this.userService.sendLoginData(loginData);
        } else {
          console.log('Unexpected error: ' + result.error);
        }
      });
  }

  public statusChangeCallback(response: any){
    if (response.status === 'connected') {
      console.log('Connection to facebook successful');
      this.me();
    } else {
      this.login();
    }
  }

  login() {
    FB.login((result) => {
      this.me();
    }, { scope: 'email, user_friends' });
  }

  facebook() {
    FB.getLoginStatus((response) => {
      this.statusChangeCallback(response);
    });
  }

  ngOnInit(): void {
    FB.init({
      appId      : '114614415891866',
      cookie     : true,  // enable cookies to allow the server to access
                          // the session
      xfbml      : true,  // parse social plugins on this page
      version    : 'v2.8' // use graph api version 2.8
    });
  }

}
