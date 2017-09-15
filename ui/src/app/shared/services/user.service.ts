import { Injectable } from '@angular/core';
import {User} from "../models/user-model";
import {Headers, Http} from "@angular/http";
import {HttpClient} from "@angular/common/http";
import {LoginData} from "../models/login-data";
import {MyAuthService} from "../../auth/my-auth-service";
import {SharedService} from "./shared.service";

@Injectable()
export class UserService {

  private headers: Headers = new Headers({'Content-Type':'application/json'});
  constructor(private http:Http, private httpClient: HttpClient, private auth: MyAuthService, private sharedService: SharedService) { }

  public registerUser(user: User) {
    const json = JSON.stringify(user);
    this.http.post('/api/user', json, {headers: this.headers}).subscribe(res => console.log(res));
  }

  public sendLoginData(data: LoginData) {
    const json = JSON.stringify(data);
    this.http.post('/api/auth/login', json, {headers: this.headers}).subscribe(res => {
      const tokenResponse = res.json();
      console.log('Server accepted login and opened session for userId: ' + tokenResponse.userId);
      this.auth.saveToken(tokenResponse.token);
      this.sharedService.notifyOther({loggedIn: true});
    });
  }

  public upgradeFreeUser(id: string) : Promise<User> {
    return this.httpClient.put<User>(`/api/user/${id}/upgradeUser`, {}).toPromise();
  }


}
