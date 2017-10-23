import {Component, OnInit} from '@angular/core';
import {News} from "../../shared/models/news";
import {UserService} from "../../shared/services/user.service";
import {User} from "../../shared/models/user-model";
import {WebSocketService} from "../../shared/services/web-socket.service";

@Component({
  selector: 'app-news-feed',
  templateUrl: './news-feed.component.html',
  styleUrls: ['./news-feed.component.css']
})
export class NewsFeedComponent implements OnInit {

  newsArray: News[] = [];
  viewer: User = JSON.parse(localStorage.getItem("user"));

  constructor(private userService: UserService,
              private wsService: WebSocketService) { }

  ngOnInit() {
    this.loadNewsFeed();
    this.listenForServerEvents((JSON.parse(localStorage.getItem("user")) as User).id);
  }

  private listenForServerEvents(id: string) {
    this.wsService.connect(id).subscribe((res) => {
      let news : News = res;
      if ((this.newsArray.map(e => e.id).indexOf(news.id) == -1) && res.redirectId === null){
        console.log('accepted');
        this.newsArray.push(news);
      }
    });
  }

  private loadNewsFeed() {
    this.userService.getNewsFeed().subscribe((res: News[]) => {
      this.newsArray = res;
    });
  }
}
