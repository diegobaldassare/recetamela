import {Injectable} from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse,
  HttpResponse
} from '@angular/common/http';
import {Observable} from "rxjs/Observable";
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import {Router} from "@angular/router";
import {SharedService} from "../shared/services/shared.service";

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(private router: Router, private sharedService: SharedService) {}

  /* Intercepts server Unauthorized, Forbidden or not found errors. Automatically redirects back to landing page. */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      return next.handle(req).do(event => {}, err => {
      if (err instanceof HttpErrorResponse && (err.status === 401)) {
        localStorage.removeItem('X-TOKEN');
        this.sharedService.notifyOther({ loggedIn: false });
        this.router.navigate(['/']);
      }
    });
  }
}
