import {Component, Input, OnInit} from '@angular/core';
import {environment} from "../../environments/environment";
import {Router} from "@angular/router";
import {Recipe} from "../shared/models/recipe/recipe";
declare const FB: any;

@Component({
  selector: 'app-facebook-share',
  templateUrl: './facebook-share.component.html',
  styleUrls: ['./facebook-share.component.css']
})
export class FacebookShareComponent implements OnInit {
  public API_URL: string = environment.production ? "http://recetamela.herokuapp.com" : "http://localhost:9000";
  @Input() recipe: Recipe;

  constructor(private router: Router) { }


  ngOnInit(): void {
    if(environment.production){
      FB.init({
        appId      : '1944355492501091',
        cookie     : true,  // enable cookies to allow the server to access
                            // the session
        xfbml      : true,  // parse social plugins on this page
        version    : 'v2.8' // use graph api version 2.8
      });
    }else {
      FB.init({
        appId      : '114614415891866',
        cookie     : true,  // enable cookies to allow the server to access
                            // the session
        xfbml      : true,  // parse social plugins on this page
        version    : 'v2.8' // use graph api version 2.8
      });
    }
  }

  fbshare() {
    FB.ui({
        method: 'share_open_graph',
        action_type: 'og.shares',
        action_properties: JSON.stringify({
          object : {
            'og:url': this.API_URL + this.router.url,
            'og:title': 'Te receto la receta! ' + this.recipe.name,
            'og:description': this.recipe.description  ,
            'og:image': 'https://i.imgur.com/bMGr7dT.png'
          }
        })
      },
      // callback
      function(response) {
        if (response && !response.error_message) {
          // then get post content
        } else {
          console.log('Something went wrong.');
        }
      });
  }

}
