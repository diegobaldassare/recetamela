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

  noMoreNews: boolean;
  newsArray: News[] = [];
  viewer: User = JSON.parse(localStorage.getItem("user"));

  constructor(private newsService: NewsService,
              private wsService: WebSocketService) { }

  ngOnInit() {
    this.loadNewsFeed();
    this.listenForServerEvents(this.viewer.id);
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
    let latestId : string = (this.newsArray.length === 0) ? '-1' : this.newsArray[this.newsArray.length -1].id;
    this.newsService.getUserNewsFeed(latestId).subscribe((res: News[]) => {
      if (res.length == 0) this.noMoreNews = true;
      res.forEach(e => this.newsArray.push(e));
    });
  }
}
