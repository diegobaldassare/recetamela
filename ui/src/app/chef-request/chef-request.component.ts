import { Component, OnInit } from '@angular/core';
import {User} from "../shared/models/user-model";
import {UserService} from "../shared/services/user.service";
import {ChefRequest} from "../shared/models/chef-request";
import {ChefRequestService} from "../shared/services/chef-request.service";
import {ToasterService} from "angular2-toaster";

@Component({
  selector: 'app-chef-request',
  templateUrl: './chef-request.component.html',
  styleUrls: ['./chef-request.component.css']
})
export class ChefRequestComponent implements OnInit {

  users: User[];
  userRequesting: ChefRequest;
  usersRequesting: ChefRequest[];

  constructor(private userService : UserService,
              public toaster: ToasterService,
              private chefRequestService : ChefRequestService) { }

  ngOnInit() {
    this.userService.getUsers().then(users => {
      this.users = users;
    });

    this.chefRequestService.getAllChefRequest().then(usersCR => {
      this.usersRequesting = usersCR;
    });

  }

  acceptRequest(user: ChefRequest){
    let chefRequest = user;
    chefRequest.answered = true;
    chefRequest.accepted = true;

    this.chefRequestService.updateChefRequest(chefRequest, chefRequest.id).then(() => {
      this.toaster.pop('success', 'Usario Aceptado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido aceptar');
    });
  }

  rejectRequest(user: ChefRequest){
    let chefRequest = user;
    chefRequest.answered = true;
    chefRequest.accepted = false;

    this.chefRequestService.updateChefRequest(chefRequest, chefRequest.id).then(() => {
      this.toaster.pop('success', 'Usario Rechazado');
    }, () => {
      this.toaster.pop('error', 'No se ha podido rechazar');
    });
  }

  openDetails(user: ChefRequest){
    this.userRequesting = user;
  }
}
