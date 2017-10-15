import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../shared/models/user-model";

@Component({
  selector: 'app-chef-request-details',
  templateUrl: './chef-request-details.component.html',
  styleUrls: ['./chef-request-details.component.css']
})
export class ChefRequestDetailsComponent implements OnInit {

  @Input() userRequest: User;

  constructor() { }

  ngOnInit() {
  }

}
