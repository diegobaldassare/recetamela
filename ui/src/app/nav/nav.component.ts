import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {MyAuthService} from "../auth/my-auth-service";
import {SharedService} from "../shared/services/shared.service";
import {isNull} from "util";
import {AsyncPipe} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {User} from "../shared/models/user-model";
import {Router} from '@angular/router';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  isLoggedIn: boolean;
  isPremium: boolean;
  user : User;

  //Both SharedService and ChangeDetectorRef are necessary to listen to changes on logged in variable to show different nav.
  constructor(private auth: MyAuthService,
              private sharedService: SharedService,
              private cdRef: ChangeDetectorRef,
              private router: Router) {
    this.isLoggedIn = !isNull(localStorage.getItem("X-TOKEN"));
    this.sharedService.notifyObservable$.subscribe((res) => {
      if (res.hasOwnProperty('loggedIn')) {
        this.isLoggedIn = res.loggedIn;
        this.cdRef.detectChanges();
        if (this.isLoggedIn) {
          auth.requestLoggedUser().then((user : User) => {
            localStorage.setItem("user", JSON.stringify(user));
            this.user = user;
            if (user.type === 'PremiumUser') this.isPremium = true;
            this.cdRef.detectChanges();
          });
        } else {
        }
        //auth.loggedUser.then(res => {this.user = res});
      }
    });
  }

  public navDropdownLogoout() {
    this.auth.logout();
  }

  public crearReceta() {
    if (this.isPremium) {
      this.router.navigate(['/recetas/crear']);
    } else {
      this.sharedService.notifyOther({upgradeForm: true});
    }
  }

  public premium() {
    this.sharedService.notifyOther({upgradeForm: true});
  }

  ngOnInit() {
    this.user = JSON.parse(localStorage.getItem("user")) as User;
    // this.isPremium = this.auth.isPremium():
  }

}
