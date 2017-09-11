import { Component, OnInit } from '@angular/core';
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {LoginData} from "../shared/models/login-data";
import {HttpModule} from "@angular/http"
declare const FB: any;

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit {

  constructor(private userService: UserService) { }

  token: any;
  loged: boolean = false;
  user = { name: 'Hello' };

  public startFacebook(): void {
    FB.init({
      appId      : '114614415891866',
      cookie     : true,  // enable cookies to allow the server to access
                          // the session
      xfbml      : true,  // parse social plugins on this page
      version    : 'v2.8' // use graph api version 2.8
    });

    FB.login(function(response) {
      if (response.authResponse) {
        console.log('Welcome!  Fetching your information.... ');
        FB.api('/me', {fields: 'name,userid,email'}, function(response) {
          console.log('Good to see you, ' + response.name + " " + response.userId + " " + response.email);
        });
      } else {
        console.log('User cancelled login or did not fully authorize.');
      }
    }, {scope: 'email, user_friends'});

    FB.getLoginStatus((response) => {
      this.statusChangeCallback(response);
    });

  }

  me() {
    FB.api('/me?fields=id,name,email,first_name,last_name,birthday,gender,picture.width(150).height(150),age_range,friends',
      (result) => {
        if (result && !result.error) {
          this.user = result;
          var u = new User(result.id, result.first_name, result.last_name, result.email, result.birthday);
          console.log(result);
          //Send backend the Login Data we got from the Facebook Response
          var loginData = new LoginData(FB.getAuthResponse()['accessToken'],
            result.email,
            result.name,
            result.gender,
            result.id);
          this.userService.sendLoginData(loginData);
        } else {
          console.log(result.error);
        }
      });
  }

  public statusChangeCallback(response: any){
    if (response.status === 'connected') {
      console.log('connected');
      this.me();
    } else {
      this.login();
    }
  }

  login() {
    FB.login((result) => {
      this.loged = true;
      this.token = result;
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
