import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {MyAuthService} from "../auth/my-auth-service";
import {SharedService} from "../shared/services/shared.service";
import {isNull} from "util";
import {User} from "../shared/models/user-model";
import {EventSourcePolyfill} from 'ng-event-source';
import {Router} from '@angular/router';
import {MessageEvent} from "../shared/models/message-event";
import {Notification} from "../shared/models/notification";
import {UserService} from "../shared/services/user.service";
import {Subscription} from "rxjs";
import {WebSocketService} from "../shared/services/web-socket.service";

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
  subscription: Subscription;

  //Both SharedService and ChangeDetectorRef are necessary to listen to changes on logged in variable to show different nav.
  constructor(private auth: MyAuthService,
              private sharedService: SharedService,
              private userService: UserService,
              private cdRef: ChangeDetectorRef,
              private router: Router,
              private wsService: WebSocketService) {
    this.isLoggedIn = !isNull(localStorage.getItem("X-TOKEN"));
    this.subscription = this.userService.getModifiedUser().subscribe(user => { this.user = user; });
    this.sharedService.notifyObservable$.subscribe((res) => {
      if (res.hasOwnProperty('loggedIn')) {
        this.isLoggedIn = res.loggedIn;
        this.cdRef.detectChanges();
        if (this.isLoggedIn) {
          this.doUpdate();
          this.listenForServerEvents((JSON.parse(localStorage.getItem("user")) as User).id);
        }
      }
      if (res.hasOwnProperty('premium')) this.updateDropdown(res.premium);
    });
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
      if (user.type != 'FreeUser') {
        this.isPremium = true;
      }
      this.cdRef.detectChanges();
    });
  }

  ngOnInit() {
    if (this.auth.isLoggedIn()) {
      this.doUpdate();
      this.listenForServerEvents((JSON.parse(localStorage.getItem("user")) as User).id);
    }
    this.user = JSON.parse(localStorage.getItem("user")) as User;
  }

  private listenForServerEvents(id: string) {
    this.wsService.connect(id).subscribe((res) => {
      if (res.header === 'notification') {
        let notification : Notification = res.object;
        if (this.notificationList.map(e => e.id).indexOf(notification.id) == -1) {
          this.notificationList.push(notification);
        }
      }
    });
  }

  notificationClicklistener(i : number) : void {
    const notification : Notification = this.notificationList.reverse()[i];
    switch (notification.title) {
      case 'SUBSCRIPTION':
        this.router.navigate([`/usuario/${notification.sender}/perfil`]);
        break;
      case 'RECIPE':
        this.router.navigate([`/recetas/${notification.redirectId}`]);
        break;
      case 'CATEGORY':
        this.router.navigate([`/recetas/${notification.redirectId}`]);
        break;
      case 'RATING':
        this.router.navigate([`/recetas/${notification.redirectId}`]);
        break;
      case 'REQUEST':
        this.router.navigate([`/usuario/${notification.receiver}/perfil`]);
        break;
      case 'COMMENT':
        this.router.navigate([`/recetas/${notification.redirectId}`]);
        break;
    }
    this.userService.markNotificationRead(notification.id);
    this.deleteNotification(i);
    this.notificationList.reverse();
  }

  deleteNotification(i: number) :void {
    this.notificationList.splice(i, 1);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private addToRecipeBook(){
    this.router.navigate(['/recetarios']);
  }

  private chefRequest(){
    this.router.navigate(['/solicitudes']);
  }

  private createNews(){
    this.router.navigate(['/noticias/crear']);
  }
}
