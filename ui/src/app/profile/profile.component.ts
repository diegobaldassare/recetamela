import {Component, NgZone, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {HttpClient} from "@angular/common/http";
import {EventSourcePolyfill} from 'ng-event-source';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private user: User;
  private loggedUser: User;
  public fetched: boolean;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private http: HttpClient,
    private zone: NgZone) {
  }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.userService.getUser(id).then(user => {
      this.user = user;
      this.fetched = true;
    }, () => { this.fetched = true });
    this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;

    let eventSource = new EventSourcePolyfill('/api/notifications', { headers: { Authorization: 'Bearer' + localStorage.getItem("X-TOKEN") } });
    eventSource.addEventListener('logged', function(e : MessageEvent) {
      console.log(e.data);
      //eventSource.close();
    }, false);
    eventSource.addEventListener('sub', function (e: MessageEvent) {
      console.log("got a new subscription");
    }, false);
  }

  sub() {
    this.http.post('api/user/sub', "").subscribe(e => {
      console.log("success on sub");
    })
  }


}
