import {Component, OnInit} from '@angular/core';
import {News} from "../../../shared/models/news";
import {NewsService} from "../../../shared/services/news-service";
import {ToasterService} from "angular2-toaster";
import {MediaService} from "../../../shared/services/media.service";
import {FormatService} from "../../../shared/services/format.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-news',
  templateUrl: './create-news.component.html',
  styleUrls: ['./create-news.component.scss']
})
export class CreateNewsComponent implements OnInit {

  private news: News = new News();
  private uploadingImage: boolean;

  constructor(
    private newsService: NewsService,
    private toaster: ToasterService,
    private mediaService: MediaService,
    private formatter: FormatService,
    private router: Router,
  ) {}

  ngOnInit() {}

  public get disabledSubmit(): boolean {
    return this.news.title.trim().length == 0 ||
      this.news.description.trim().length == 0 ||
      (this.news.videoUrl.length > 0 && !this.validVideoUrl) ||
      this.uploadingImage;
  }

  private uploadImage(e: Event) {
    e.preventDefault();
    this.uploadingImage = true;
    const files = (<HTMLInputElement> document.getElementById('image')).files;
    if (!files.length) return;
    this.mediaService.uploadMedia(files[0]).then(media => {
      this.uploadingImage = false;
      (<HTMLInputElement> document.getElementById('image')).value = '';
      this.news.image = media;
    }, () => { (<HTMLInputElement> document.getElementById('image')).value = ''; });
  }

  private get videoThumbnailUrl(): string {
    return this.formatter.youtubeThumbnailUrl(this.news.videoUrl);
  }

  private get validVideoUrl(): boolean {
    return this.formatter.isValidYoutubeUrl(this.news.videoUrl);
  }

  private create() {
    this.newsService.createNews(this.news).then((news) => {
      this.toaster.pop('success', 'Noticia creada');
      this.news = new News();
      this.router.navigate([`/noticias/${news.id}`]);
    }, () => {
      this.toaster.pop('error', 'Noticia no creada');
    });
  }
}
