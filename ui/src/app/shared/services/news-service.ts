import {ApiService} from "./api-service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {News} from "../models/news";

@Injectable()
export class NewsService extends ApiService {

  private URL: string = `${ApiService.API_URL}/news`;

  constructor(private http: HttpClient) {
    super();
  }

  public createNews(news: News): Promise<News> {
    return this.http.post<any>(`${this.URL}`, news).toPromise();
  }

  public deleteNews(id: string): Promise<any> {
    return this.http.delete<any>(`${this.URL}/${id}/delete`).toPromise();
  }
}
