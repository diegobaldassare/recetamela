import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {User} from "../shared/models/user-model";

@Injectable()
export class MyAuthService {

  private _loggedUser: User;

  constructor(private http: HttpClient ) {
  }

  public logout() {
      this.http.post('/api/auth/logout', "logout").subscribe(res => {
        console.log(res);
      })
  }

  get loggedUser(): Promise<User> {
    return this._loggedUser ? Promise.resolve(this._loggedUser) : this.requestLoggedUser();
  }

  private requestLoggedUser(): Promise<User> {
    return this.http.get<User>('/api/auth/logged-data').toPromise().then(resData => {
      const user = resData as User;
      this._loggedUser = user;
      return user;
    })
  }

  get isLoggedIn(): boolean {
    console.log("Logged: " + localStorage.getItem("X-TOKEN") !== null);
    return localStorage.getItem("X-TOKEN") !== null;
  }

  public getAuthorizationHeader(): string {
    return localStorage.getItem("X-TOKEN");
  }

  public saveToken(token: string) {
    localStorage.setItem("X-TOKEN", token);
  }
}
