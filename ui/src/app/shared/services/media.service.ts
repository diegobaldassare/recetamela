import {Injectable} from "@angular/core";
import 'rxjs/add/operator/map';
import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";
import {Media} from "../models/media";

@Injectable()
export class MediaService extends ApiService {

  private URL: string = `${ApiService.API_URL}/media`;

  constructor(private http: HttpClient) {
    super();
  }

  public uploadMedia(file: Blob): Promise<Media> {
    const data = new FormData();
    data.append('file', file);
    return this.http.post<Media>(this.URL, data).toPromise();
  }
}
