import {CanActivate, Router} from "@angular/router";
import {Injectable} from "@angular/core";
import { User } from '../shared/models/user-model';

@Injectable()
export class ChefGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate() {
    const viewer: User = JSON.parse(localStorage.getItem("user"));
    if (viewer.type == 'ChefUser') return true;
    this.router.navigate(['/']);
  }
}
