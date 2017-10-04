import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";

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
  subscribed: boolean;


  constructor(
    private route: ActivatedRoute,
    private userService: UserService) {

  }

  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          const id = params['id'];
          this.userService.getUser(id).then(user => {
            this.user = user;
            this.fetched = true;
            this.loggedUser = JSON.parse(localStorage.getItem("user")) as User;
            this.fetchFollowers();
            this.fetchFollowing();
          }, () => { this.fetched = true });

        }
      );

  }

  subscribe() {
    this.userService.followUser(this.user.id).subscribe((res : User) => {
      this.followers.push(res);
      this.subscribed = true;
    });
  }

  unSubscribe() {
    this.userService.unFollowUser(this.user.id).subscribe((res : User) => {
      const index = this.followers.map(u => u.id).indexOf(res.id, 0);
      if (index > -1) {
        this.followers.splice(index, 1);
        this.subscribed = false;
      }
    });
  }

  fetchFollowers() {
    this.userService.getFollowers(this.route.snapshot.params['id']).subscribe((res : User[]) => {
      this.followers = res;
      this.subscribed = this.user.id !== this.loggedUser.id && (this.followers.map(u => u.id).indexOf(this.loggedUser.id, 0) > -1);
    });
  }

  fetchFollowing() {
    this.userService.getFollowing(this.route.snapshot.params['id']).subscribe((res : User[]) => {
      this.following = res;
    });
  }


}
