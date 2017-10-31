import {ApiService} from "./api-service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {News} from "../models/news";
import {Observable} from "rxjs";

@Injectable()
export class NewsService extends ApiService {

  private URL: string = `${ApiService.API_URL}/news`;

  constructor(private http: HttpClient) {
    super();
  }

  public createNews(news: News): Promise<News> {
    return this.http.post<News>(`${this.URL}`, news).toPromise();
  }

  public deleteNews(id: string): Promise<any> {
    return this.http.delete<any>(`${this.URL}/${id}`).toPromise();
  }

  public getNews(id: string): Promise<News> {
    return this.http.get<News>(`${this.URL}/${id}`).toPromise();
  }

  public getUserNewsFeed(id: string) : Observable<News[]> {
    return this.http.get<News[]>(`/api/user/newsfeed/${id}`);
  }

  public getUserNews(id: string): Promise<News[]> {
    return this.http.get<News[]>(`/api/user/${id}/news`).toPromise();
  }
}
