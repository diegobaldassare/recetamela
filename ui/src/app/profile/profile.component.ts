import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private user: User;
  public fetched: boolean;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService) {
  }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.userService.getUser(id).then(user => {
      this.user = user;
      this.fetched = true;
    }, () => { this.fetched = true });
  }
}
