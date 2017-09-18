import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {SharedService} from "../shared/services/shared.service";
import {MyAuthService} from "../auth/my-auth-service";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnDestroy {

  public loggedIn: boolean = this.authService.isLoggedIn();
  public sub: Subscription;

  constructor(
    private authService: MyAuthService,
    private sharedService: SharedService,
    private cdRef: ChangeDetectorRef
  ) {
    this.sub = this.sharedService.notifyObservable$.subscribe((res) => {
      if (res.hasOwnProperty('loggedIn')) {
        window.location.reload(); //TODO fix
        this.loggedIn = res.loggedIn;
        this.cdRef.detectChanges();
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

}
