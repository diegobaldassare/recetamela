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
        if (this.isLoggedIn) this.doUpdate();
      }
      if (res.hasOwnProperty('premium')) this.updateDropdown(res.premium);
    });
    /*this.sharedService.notifyObservable$.subscribe(res => {
      if (res.hasOwnProperty('isPremium')) {
        this.isPremium = res.isPremium;
        this.cdRef.detectChanges();
      }
    })*/
  }

  public navDropdownLogoout() {
    this.auth.logout();
  }

  public updateDropdown(value: boolean) : void {
    this.isPremium = value;
    this.cdRef.detectChanges();
  }

  public myProfile() {
    this.router.navigate([`/usuario/${this.user.id}/perfil`]);
  }

  public crearReceta() {
    if (this.isPremium) {
      this.router.navigate(['/recetas/crear']);
    } else {
      this.premium();
    }
  }

  public premium() {
    this.sharedService.notifyOther({upgradeForm: true, expired: false});
  }

  private doUpdate() {
    this.auth.requestLoggedUser().then((user : User) => {
      localStorage.setItem("user", JSON.stringify(user));
      this.user = user;
      if (user.type === 'PremiumUser') {
        this.isPremium = true;
      }
      this.cdRef.detectChanges();
    });
  }

  ngOnInit() {
    if (this.auth.isLoggedIn()) {
      this.doUpdate();
    }
    this.user = JSON.parse(localStorage.getItem("user")) as User;
    // this.isPremium = this.auth.isPremium():
  }

}
