import {Injectable} from "@angular/core";
import {ApiService} from "./api-service";
import {HttpClient} from "@angular/common/http";
import {ChefRequest} from "../models/chef-request";
import {BooleanResponse} from "../models/boolean-response";

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

  isUserChefRequest() : Promise<BooleanResponse> {
    return this.http.get<BooleanResponse>(`${this.URL}/sent/`).toPromise();
  }

  isUserChefRequestAccepted() : Promise<BooleanResponse> {
    return this.http.get<BooleanResponse>(`${this.URL}/accepted/`).toPromise();
  }

  getAllChefRequest() : Promise<ChefRequest[]> {
    return this.http.get<ChefRequest[]>(`${this.URL}/all/`).toPromise();
  }

  getAllChefRequestUnanswered() : Promise<ChefRequest[]> {
    return this.http.get<ChefRequest[]>(`${this.URL}/all/unanswered`).toPromise();
  }

  deleteChefRequest(chefRequestId: string) : Promise<any> {
    return this.http.delete<any>(`${this.URL}/${chefRequestId}/delete`).toPromise();
  }
}
