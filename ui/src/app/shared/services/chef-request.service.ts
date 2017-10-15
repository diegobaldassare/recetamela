import {Injectable} from "@angular/core";
import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";
import {ChefRequest} from "../models/chef-request";

@Injectable()
export class ChefRequestService extends ApiService {

  private URL: string = `${ApiService.API_URL}/chefrequest`;

  constructor(private http: HttpClient) {
    super();
  }

  createChefRequest(chefRequest: ChefRequest) : Promise<ChefRequest> {
    return this.http.post<ChefRequest>(`${this.URL}/create`, chefRequest).toPromise();
  }

  updateChefRequest(chefRequest: ChefRequest, chefRequestId: string) : Promise<ChefRequest> {
    return this.http.put<ChefRequest>(`${this.URL}/${chefRequestId}/update`, chefRequest).toPromise();
  }

  getChefRequest(chefRequestId: string) : Promise<ChefRequest> {
    return this.http.get<ChefRequest>(`${this.URL}/${chefRequestId}`).toPromise();
  }

  isUserChefRequest() : Promise<boolean> {
    return this.http.get<boolean>(`${this.URL}/sent/`).toPromise();
  }

  isUserChefRequestAccepted() : Promise<boolean> {
    return this.http.get<boolean>(`${this.URL}/accepted/`).toPromise();
  }

  getAllChefRequest() : Promise<ChefRequest[]> {
    return this.http.get<ChefRequest[]>(`${this.URL}/all/`).toPromise();
  }

  deleteChefRequest(chefRequestId: string) : Promise<any> {
    return this.http.delete<any>(`${this.URL}/${chefRequestId}/delete`).toPromise();
  }
}
