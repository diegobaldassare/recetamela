import {Injectable} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse} from '@angular/common/http';
import {Observable} from "rxjs/Observable";
import {MyAuthService} from "./my-auth-service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Get the auth header from the service.
    const authHeader = localStorage.getItem("X-TOKEN");
    // Clone the request to add the new header.
    const authReq = req.clone({headers: req.headers.set('Authorization', 'Bearer ' + authHeader)});
    // Pass on the cloned request instead of the original request.
    return next.handle(authReq);
  }
}
