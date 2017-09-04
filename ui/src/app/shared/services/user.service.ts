import { Injectable } from '@angular/core';
import {User} from "../models/user-model";
import {Http, Headers} from "@angular/http";

@Injectable()
export class UserService {

  private headers: Headers = new Headers({'Content-Type':'application/json'});
  constructor(private http:Http) { }

  public registerUser(user: User) {
    const json = JSON.stringify(user);
    console.log("Logging json");
    console.log(json);
    this.http.post('/api/user', json, {headers: this.headers}).subscribe(res => console.log(res));
  }


}
