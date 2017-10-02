import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {MyAuthService} from "../auth/my-auth-service";
import {SharedService} from "../shared/services/shared.service";
import {isNull} from "util";
import {User} from "../shared/models/user-model";
import {EventSourcePolyfill} from 'ng-event-source';
import {Router} from '@angular/router';
import {MessageEvent} from "../shared/models/message-event";
import {Notification} from "../shared/models/notification";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit, OnDestroy {


  isLoggedIn: boolean;
  isPremium: boolean;
  user : User;
  private eventSource : EventSourcePolyfill;
  notificationList: Notification[] = [];

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
          this.doUpdate();
          this.listenForServerEvents();
        }
        else {
          this.unsubscribeFromServerEvents();
        }
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
      this.listenForServerEvents();
    }
    this.user = JSON.parse(localStorage.getItem("user")) as User;
  }

  private listenForServerEvents() {
    this.eventSource = new EventSourcePolyfill('/api/notifications', { headers: { Authorization: 'Bearer' + localStorage.getItem("X-TOKEN") } });
    this.eventSource.addEventListener('SUBSCRIPTION',(e: MessageEvent) => {
      let notification : Notification = JSON.parse(e.data) as Notification;
      this.notificationList.push(notification);
    }, false);
  }

  notificationClicklistener(i : number) : void {
    // Redirect user to sender profile if notification was clicked.
    // To do: Manage each different notifcation type. For example: If a new recipe is created, redirect to recipe.
    const senderId = this.notificationList[i].sender;
    this.router.navigate([`/usuario/${senderId}/perfil`]);
    this.notificationList.splice(i, 1);
  }

  unsubscribeFromServerEvents() {
    this.eventSource.close();
  }

  ngOnDestroy(): void {
    this.unsubscribeFromServerEvents();
  }

  private addToRecipeBook(){
    this.router.navigate(['/recetarios']);
  }

}
