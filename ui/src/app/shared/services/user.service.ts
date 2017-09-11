import { Injectable } from '@angular/core';
import {User} from "../models/user-model";
import {Http, Headers} from "@angular/http";
import {LoginData} from "../models/login-data";
import {AuthService} from "../../auth/auth.service";

@Injectable()
export class UserService {

  private headers: Headers = new Headers({'Content-Type':'application/json'});
  constructor(private http:Http, private auth:AuthService) { }

  public registerUser(user: User) {
    const json = JSON.stringify(user);
    this.http.post('/api/user', json, {headers: this.headers}).subscribe(res => console.log(res));
  }

  public sendLoginData(data: LoginData) {
    const json = JSON.stringify(data);
    this.http.post('/api/auth/login', json, {headers: this.headers}).subscribe(res => {
      const tokenResponse = res.json();
      console.log(tokenResponse);
      console.log("Saving: " + tokenResponse.token);
      this.auth.saveToken(tokenResponse.token);
    });
  }


}
