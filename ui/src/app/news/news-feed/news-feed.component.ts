import {Component, OnInit} from '@angular/core';
import {News} from "../../shared/models/news";
import {UserService} from "../../shared/services/user.service";
import {User} from "../../shared/models/user-model";
import {WebSocketService} from "../../shared/services/web-socket.service";
import {NewsService} from "../../shared/services/news-service";

@Component({
  selector: 'app-news-feed',
  templateUrl: './news-feed.component.html',
  styleUrls: ['./news-feed.component.css']
})
export class NewsFeedComponent implements OnInit {

  limit: number = 5;
  newsArray: News[] = [];
  viewer: User = JSON.parse(localStorage.getItem("user"));

  constructor(private newsService: NewsService,
              private wsService: WebSocketService) { }

  ngOnInit() {
    this.loadNewsFeed();
    this.listenForServerEvents((JSON.parse(localStorage.getItem("user")) as User).id);
  }

  private listenForServerEvents(id: string) {
    this.wsService.connect(id).subscribe((res) => {
      if (res.header == 'news') {
        let news : News = res.object;
        if ((this.newsArray.map(e => e.id).indexOf(news.id) == -1)){
          this.newsArray.unshift(news);
        }
      }
    });
  }

  private loadNewsFeed() {
    this.newsService.getUserNewsFeed().subscribe((res: News[]) => {
      this.newsArray = res;
    });
  }

  private growLimit() {
    this.limit += 3;
  }
}
