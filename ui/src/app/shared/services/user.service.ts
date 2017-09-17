import { Injectable } from '@angular/core';
import {User} from "../models/user-model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginData} from "../models/login-data";
import {MyAuthService} from "../../auth/my-auth-service";
import {SharedService} from "./shared.service";
import {AuthToken} from "../models/auth-token";
import {Router} from "@angular/router";
import {ToasterService} from "angular2-toaster";
import {CheckExpirationDateResponse} from "../models/ced-response";

@Injectable()
export class UserService {

  private headers: HttpHeaders = new HttpHeaders({'Content-Type':'application/json'});
  constructor(private http:HttpClient, private router: Router, private toaster: ToasterService, private auth: MyAuthService, private sharedService: SharedService) { }

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
      this.router.navigate(['/home']);
      this.checkExpirationDate(tokenResponse.userId).then(response => {
        localStorage.setItem("user", JSON.stringify(response.user));
        this.sharedService.notifyOther({loggedIn: true});
        if (!response.expired) return;
        this.sharedService.notifyOther({upgradeForm: true, expired: true});
        this.sharedService.notifyOther({premium: false});
      });
    });
  }

  public upgradeFreeUser(id: string) : Promise<User> {
    return this.http.put<User>(`/api/user/${id}/upgradeUser`, {}).toPromise();
  }

  public checkExpirationDate(id: string) : Promise<CheckExpirationDateResponse> {
    return this.http.put<CheckExpirationDateResponse>(`/api/premiumuser/${id}/checkDate`, {}).toPromise();
  }

}
