import {Injectable} from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse,
  HttpResponse
} from '@angular/common/http';
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).do(event => {}, err => {
      console.log('Intercepting error? ' + (err instanceof HttpErrorResponse));
      if (err instanceof HttpErrorResponse && (err.status === 401 || err.status === 404 || err.status === 403)) {
        console.log('Catching error');
        if (err.status === 401) {
          console.log("Catching error ");
          //localStorage.removeItem('token');
        }
        //this.router.navigate(['landing']);
      }
    });
  }
}
