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

    this.chefRequestService.getAllChefRequestUnanswered().then(usersCR => {
      this.usersRequesting = usersCR;
    });

  }

  acceptRequest(request: ChefRequest){
    let chefRequest = request;
    chefRequest.answered = true;
    chefRequest.accepted = true;

    this.chefRequestService.updateChefRequest(chefRequest, chefRequest.id).then(() => {
      this.toaster.pop('success', 'Usuario Aceptado');

      this.userService.upgradeToChefUser(chefRequest.user.id).then(() => {
        this.toaster.pop('success', 'Usuario Actualizado');

        let usersChefRequestsIndex;   //Saco el elemento de la lista del front
        let userId
        for (let i=0; i<this.usersRequesting.length; i++){
          if(this.usersRequesting[i].id == request.id){
            usersChefRequestsIndex = i;
            userId = request.user.id
          }
        }
        if (usersChefRequestsIndex > -1) {
          this.usersRequesting.splice(usersChefRequestsIndex, 1);
        }

        for (let i=0; i<this.users.length; i++){    //Cambio el tipo del usuario de la lista del front
          if(this.users[i].id == userId){
            this.users[i].type = "ChefUser"
          }
        }

      }, () => {
        this.toaster.pop('error', 'No se ha podido actualizar');
      });


    }, () => {
      this.toaster.pop('error', 'No se ha podido aceptar');
    });
  }

  rejectRequest(request: ChefRequest){
    let chefRequest = request;
    chefRequest.answered = true;
    chefRequest.accepted = false;

    this.chefRequestService.updateChefRequest(chefRequest, chefRequest.id).then(() => {
      this.toaster.pop('success', 'Usuario Rechazado');

      let index;                      //Saco el elemento de la lista del front
      for (let i=0; i<this.usersRequesting.length; i++){
        if(this.usersRequesting[i].id == request.id){
          index = i;
        }
      }
      if (index > -1) {
        this.usersRequesting.splice(index, 1);
      }

    }, () => {
      this.toaster.pop('error', 'No se ha podido rechazar');
    });
  }

  openDetails(user: ChefRequest){
    this.userRequesting = user;
  }
}
