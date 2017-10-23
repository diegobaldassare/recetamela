import {Component, OnInit} from '@angular/core';
import {News} from "../../shared/models/news";
import {UserService} from "../../shared/services/user.service";
import {User} from "../../shared/models/user-model";

@Component({
  selector: 'app-news-feed',
  templateUrl: './news-feed.component.html',
  styleUrls: ['./news-feed.component.css']
})
export class NewsFeedComponent implements OnInit {

  newsArray: News[] = [];
  viewer: User = JSON.parse(localStorage.getItem("user"));

  constructor(private userService: UserService,) { }

  ngOnInit() {
    this.loadNewsFeed();
  }

  private loadNewsFeed() {
    this.userService.getNewsFeed().subscribe((res: News[]) => {
      this.newsArray = res;
    });
  }
}
