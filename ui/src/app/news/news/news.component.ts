import {Component} from '@angular/core';
import {News} from "../../shared/models/news";
import {NewsService} from "../../shared/services/news-service";
import {DomSanitizer} from "@angular/platform-browser";
import {ToasterService} from "angular2-toaster";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../shared/models/user-model";

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent {

  private news: News;
  private fetched: boolean;
  private viewer: User = JSON.parse(localStorage.getItem("user"));
  private embedVideoUrl;

  constructor(
    private newsService: NewsService,
    private sanitizer: DomSanitizer,
    private toaster: ToasterService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.newsService.getNews(this.route.snapshot.params['id']).then(n => {
      this.news = n;
      this.news.created = new Date(n.created);
      if (this.news.videoUrl) this.embedVideoUrl = this.setEmbedVideoUrl(this.news.videoUrl);
      this.fetched = true;
    }, () => this.fetched = true);
  }

  private deleteNews() {
    this.newsService.deleteNews(this.news.id).then(() => {
      this.toaster.pop("success", "Noticia eliminada");
      this.router.navigate(['/noticias']);
    }, () => this.toaster.pop("error", "Noticia no eliminada"));
  }

  private setEmbedVideoUrl(u) {
    const split = u.split('v=');
    const url = `https://www.youtube.com/embed/${split[1]}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
}
