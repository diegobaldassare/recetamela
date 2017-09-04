import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import 'rxjs/add/operator/map';
import {ApiService} from "./api-service";

@Injectable()
export class MediaService extends ApiService {
  
  private URL: string = `${ApiService.API_URL}/media`;

  constructor(private http: Http) {
    super();
  }

  public uploadMedia(file: Blob): Promise<any> {
    const data = new FormData();
    data.append('file', file);
    return this.http.post(this.URL, data)
      .map((r: Response) => r.json()).toPromise();
  }
}
