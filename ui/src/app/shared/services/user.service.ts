import { Injectable } from '@angular/core';
import {User} from "../models/user-model";
import {Http, Headers} from "@angular/http";
import {UserToken} from "../models/user-token";

@Injectable()
export class UserService {

  private headers: Headers = new Headers({'Content-Type':'application/json'});
  constructor(private http:Http) { }

  public registerUser(user: User) {
    const json = JSON.stringify(user);
    this.http.post('/api/user', json, {headers: this.headers}).subscribe(res => console.log(res));
  }

  public hashToken(token: UserToken) {
    const json = JSON.stringify(token);
    this.http.post('/api/auth/login', json, {headers: this.headers}).subscribe(res => console.log(res));
  }


}
