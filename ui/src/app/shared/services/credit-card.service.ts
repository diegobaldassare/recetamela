import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http"
import {ApiService} from "./api-service";
import {CreditCard} from "../models/payment/credit-card";

@Injectable()
export class CreditCardService extends ApiService {

  private URL: string = `${ApiService.API_URL}/creditcard`;

  constructor(private http: HttpClient){
    super();
  }

  createCreditCard(creditcard: CreditCard): Promise<CreditCard> {
    return this.http.post<CreditCard>(`${this.URL}/create`, creditcard).toPromise();
  }

  getCreditCard(id: string): Promise<CreditCard> {
    return this.http.get<CreditCard>(`${this.URL}/${id}`).toPromise();
  }

  getUserCreditCards(): Promise<CreditCard[]> {
    return this.http.get<CreditCard[]>(`${this.URL}/all`).toPromise();
  }

  modifyCreditCard(id: string, creditcard: CreditCard): Promise<CreditCard> {
    return this.http.put<CreditCard>(`${this.URL}/${id}/update`, creditcard).toPromise();
  }

  deleteRecipe(id: string) : Promise<any> {
    return this.http.delete(`${this.URL}/${id}/delete`).toPromise();
  }
}
