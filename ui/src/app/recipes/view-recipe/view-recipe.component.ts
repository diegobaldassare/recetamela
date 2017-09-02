import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {log} from "util";

@Component({
  selector: 'app-view-recipe',
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.css']
})
export class ViewRecipeComponent implements OnInit {

  private id; recipe; fetched;

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit() {
    this.id = +this.route.snapshot.params['id'];
    this.http.get(`http://localhost:9000/api/recipe/${this.id}`).subscribe(data => {
      this.recipe = data;
      this.fetched = true;
    }, () => { this.fetched = true });
  }
}
