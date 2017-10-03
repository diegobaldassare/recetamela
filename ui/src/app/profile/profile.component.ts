import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {


  private user: User;
  private loggedUser: User;
  public fetched: boolean;
  followers: User[] = [];
  following: User[] = [];


  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private http: HttpClient,) {
  }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.userService.getUser(id).then(user => {
      this.user = user;
      this.fetched = true;
    }, () => { this.fetched = true });
    this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
  }

  sub() {
    this.http.post(`/api/user/subscribe/${this.user.id}`, "").subscribe(e => {
      // Do something with response
    });
  }

  fetchFollowers() {
    this.userService.getFollowers(this.route.snapshot.params['id']).then((res : User[]) => {
      this.followers = res;
    })
  }

  fetchFollowing() {
    this.userService.getFollowing(this.route.snapshot.params['id']).then((res : User[]) => {
      console.log("following " + res);
      this.following = res;
    })
  }


}
