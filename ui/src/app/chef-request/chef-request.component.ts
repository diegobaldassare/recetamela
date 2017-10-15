import { Component, OnInit } from '@angular/core';
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";

@Component({
  selector: 'app-chef-request',
  templateUrl: './chef-request.component.html',
  styleUrls: ['./chef-request.component.css']
})
export class ChefRequestComponent implements OnInit {

  users: User[];
  userRequesting: User;

  constructor(private userService : UserService) { }

  ngOnInit() {
    this.userService.getUsers().then(users => {
      this.users = users;
    })

  }

  acceptRequest(id: string){
    console.log(id);
  }

  rejectRequest(id: string){
    console.log(id);
  }

  openDetails(user: User){
    this.userRequesting = user;
  }
}
