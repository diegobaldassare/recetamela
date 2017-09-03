import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import 'rxjs/add/operator/map'

@Injectable()
export class MediaService {

  constructor(private http: Http) {}

  public uploadMedia(file: Blob): Promise<any> {
    const data = new FormData();
    data.append('file', file);
    return this.http.post("http://localhost:9000/api/media", data)
      .map((r: Response) => r.json()).toPromise();
  }
}
