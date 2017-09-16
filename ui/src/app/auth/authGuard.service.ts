import {CanActivate, Router} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate() {
    const token = localStorage.getItem("X-TOKEN");
    if (token) {
      return true;
    }
    this.router.navigate(['/']);
  }
}
