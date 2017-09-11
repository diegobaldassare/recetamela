import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class MyAuthService {

  constructor(private http: HttpClient ) {
  }

  public logout() {
      this.http.post('/api/auth/logout', "logout").subscribe(res => {
        console.log(res);
      })
  }

  public getAuthorizationHeader(): string {
    return localStorage.getItem("X-TOKEN");
  }

  public saveToken(token: string) {
    localStorage.setItem("X-TOKEN", token);
  }
}
