import { Injectable } from '@angular/core';
import { CookieService } from 'angular2-cookie/services/cookies.service';
import 'rxjs/add/operator/toPromise';
import { HttpService } from '../shared/services/http.service';
import {User} from '../shared/models/user-model';
import {UserCredentials} from '../shared/models/user-credentials';

const HEADERS = {'Content-Type': 'application/json'};

@Injectable()
export class AuthService {

    private tokenCookieKey = 'template-t0k3n';

    private _redirectUrl: string;
    private _token: string;
    private _loggedUser: User;

    get isLoggedIn(): boolean {
        return this._token !== '';
    }

    /*public logout() {
      this.httpClient.post('/api/auth/logout', "logout").subscribe(res => {
        console.log(res);
      })
    }*/

    public getAuthorizationHeader(): string {
      return localStorage.getItem("X-TOKEN");
    }

    public saveToken(token: string) {
      localStorage.setItem("X-TOKEN", token);
    }

    get redirectUrl(): string { return this._redirectUrl; }
    set redirectUrl(value: string) { this._redirectUrl = value; }

    get loggedUser(): Promise<User> {
        return this._loggedUser ? Promise.resolve(this._loggedUser) : this.requestLoggedUser();
    }

    constructor(private http: HttpService, private cookieService: CookieService) {
        this._token = this.getToken() || '';
        this.http.authToken = this._token;
        if (this._token) {
            this.requestLoggedUser();
        } else {
            this._loggedUser = undefined;
        }
    }

    public login(credentials: UserCredentials): Promise<Response> {
        return this.http.defaultHttp.post('/api/login', credentials.asJsonString(), this.http.defaultOptions).toPromise()
            .then(res => {
                const data  = res.json();

                this._token = res.headers.get('authorization') || '';
                this.http.authToken = this._token;
                this.cookieService.put(this.tokenCookieKey, this._token);

                return res;
            })
            .catch(this.handleError);
    }

    private getToken(): string {
        return this.cookieService.get(this.tokenCookieKey);
    }

    private requestLoggedUser(): Promise<User> {
        return this.http.get('/api/logged-data')
            .then(resData => {
                const user = resData.json() as User;
                this._loggedUser = user;
                return user;
            })
            .catch(this.handleError);
    }

    private clearSession() {
        this._token = '';
        this.http.authToken = this._token;
        this._loggedUser = undefined;
        this.cookieService.remove(this.tokenCookieKey);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }
}
