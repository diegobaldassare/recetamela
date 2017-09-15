import {CanActivate, Router} from "@angular/router";
import {Injectable} from "@angular/core";
import {User} from "../shared/models/user-model";

@Injectable()
export class PremiumGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate() {
    const user : User = JSON.parse(localStorage.getItem("user"));
    if (user.type === 'PremiumUser') {
      return true;
    }
    this.router.navigate(['/']);
  }
}
