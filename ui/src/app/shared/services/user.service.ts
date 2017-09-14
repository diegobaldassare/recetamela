import { Injectable } from '@angular/core';
import {User} from "../models/user-model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginData} from "../models/login-data";
import {MyAuthService} from "../../auth/my-auth-service";
import {SharedService} from "./shared.service";
import {AuthToken} from "../models/auth-token";

@Injectable()
export class UserService {

  private headers: HttpHeaders = new HttpHeaders({'Content-Type':'application/json'});
  constructor(private http:HttpClient, private auth: MyAuthService, private sharedService: SharedService) { }

  public registerUser(user: User) {
    const json = JSON.stringify(user);
    this.http.post('/api/user', json, {headers: this.headers}).subscribe(res => console.log(res));
  }

  public sendLoginData(data: LoginData) {
    const json = JSON.stringify(data);
    this.http.post<AuthToken>('/api/auth/login', json, {headers: this.headers}).subscribe(res => {
      const tokenResponse = res;
      console.log('Server accepted login and opened session for userId: ' + tokenResponse.userId);
      this.auth.saveToken(tokenResponse.token);
      this.sharedService.notifyOther({loggedIn: true});
    });
  }


}
