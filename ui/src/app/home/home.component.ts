import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {SharedService} from "../shared/services/shared.service";
import {MyAuthService} from "../auth/my-auth-service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private loggedIn: boolean = this.authService.isLoggedIn();

  constructor(
    private authService: MyAuthService,
    private sharedService: SharedService,
    private cdRef: ChangeDetectorRef
  ) {
    this.sharedService.notifyObservable$.subscribe((res) => {
      if (res.hasOwnProperty('loggedIn')) {
        this.loggedIn = res.loggedIn;
        this.cdRef.detectChanges();
      }
    });
  }

  ngOnInit() {}

}
